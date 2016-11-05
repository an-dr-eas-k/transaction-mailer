package net.andreask.transactionmailer.business;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.andreask.transactionmailer.domain.AccountConnection;
import net.andreask.transactionmailer.domain.AccountTransaction;
import net.andreask.transactionmailer.integration.db.AccountConnectionFacade;

@RequestScoped
@Named
public class AccountConnectionManager implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  static Logger logger = LogManager.getLogger(AccountConnectionManager.class);

  @Inject
  AccountConnectionFacade accountConnectionFacade;

  // @Inject
  // AccountTransactionFacade accountTransactionFacade;

  public void initTimerService(TimerService timerService) {

    Map<String, Timer> timerAcMap = createSearchTimerMap(timerService);
    List<String> validAccounts = processAccountConnections(timerService, timerAcMap);
    cancelRemovedTimers(timerService, validAccounts);

  }

  private Stream<Timer> createLocalActiveTimerStream(TimerService timerService) {
    return timerService
        .getAllTimers()
        .stream()
        .filter(timer -> timer.getInfo() != null && timer.getInfo() instanceof AccountConnection);
  }

  private Map<String, Timer> createSearchTimerMap(TimerService timerService) {
    return createLocalActiveTimerStream(timerService)
        .collect(Collectors.toMap(
            timer -> ((AccountConnection) timer.getInfo()).getGeneratedIban(),
            timer -> timer));
  }

  private List<String> processAccountConnections(TimerService timerService, Map<String, Timer> timerAcMap) {
    List<String> validAccounts = new ArrayList<>();
    accountConnectionFacade
        .findAll()
        .stream()
        // .peek(ac -> logger.debug("found accound in database {}", ac))
        .forEach(
            ac -> {
              try {
	        Timer existingTimer = timerAcMap.get(ac.getGeneratedIban());
	        boolean createSchedule = false;
	        if (existingTimer == null || isTimerExpired(existingTimer)) {
	          createSchedule = true;
	          logger.info("found new account {}, schedule: {}",
	              ac.getGeneratedIban(),
	              ac.getCronScheduleExpression());

	        } else if (!((AccountConnection) existingTimer.getInfo()).getCronScheduleExpression()
	            .equals(ac.getCronScheduleExpression())) {
	          createSchedule = true;
	          logger.info("updating schedule for {}: {} (new), {} (old)",
	              ac.getGeneratedIban(),
	              ac.getCronScheduleExpression(),
	              ((AccountConnection) existingTimer.getInfo()).getCronScheduleExpression());
	          existingTimer.cancel();
	        }
	        if (createSchedule) {
	          timerService.createCalendarTimer(
	              ac.getScheduleExpression(),
	              new TimerConfig(ac, false));
	        }
	        validAccounts.add(ac.getGeneratedIban());
              } catch (Exception e) {
	        logger.warn("illegal accountConnection: {} (blz) {} (ac), reason {}",
	            ac.getBankCode(), ac.getAccountNumber(), e.getMessage());
	        logger.warn(e);
	        // this.accountConnectionFacade.remove(ac);
              }
            });
    return validAccounts;
  }

  private boolean isTimerExpired(Timer t) {
    try {
      t.getInfo();
    } catch (NoSuchObjectLocalException e) {
      logger.info("timer expired");
      return true;
    }
    return false;
  }

  private void cancelRemovedTimers(TimerService timerService, List<String> validAccounts) {
    createLocalActiveTimerStream(timerService)
        .filter(timer -> !validAccounts.contains(((AccountConnection) timer.getInfo()).getGeneratedIban()))
        .peek(timer -> logger.debug("canceling timer for account {}",
            ((AccountConnection) timer.getInfo()).getGeneratedIban()))
        .forEach(Timer::cancel);
  }

  public List<AccountConnection> getAllAccountConnections() {
    return this.accountConnectionFacade.findAll();
  }

  public void save(AccountConnection accountConnection) {
    this.accountConnectionFacade.save(accountConnection);
  }

  public AccountConnection queryFromId(Integer editId) {
    return this.accountConnectionFacade.find(editId);
  }

  public void delete(int primaryKey) {
    this.accountConnectionFacade.remove(queryFromId(primaryKey));
  }

  public void export(int parseInt) {

    FacesContext fc = FacesContext.getCurrentInstance();
    HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();

    response.reset();
    response.setContentType("text/comma-separated-values");

    try (BufferedWriter output = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))) {
      AccountConnection ac = accountConnectionFacade.find(parseInt);
      response.setHeader("Content-Disposition",
          String.format("attachment; filename=\"%s-transactions.csv\"", ac.getTitle()));
      output.write(AccountTransaction.toCSVHeader());
      output.newLine();
      ac
          .getTransactions()
          .stream()
          .sorted((AccountTransaction a, AccountTransaction b) -> {
	    return Integer.compare(a.getId(), b.getId());
          })
          .forEach(at -> {
	    try {
	      output.write(at.toCSV());
	      output.newLine();
	    } catch (Exception e) {
	      throw new RuntimeException(e);
	    }
          });

      output.flush();
      output.close();

      fc.responseComplete();
    } catch (IOException e) {
      logger.error(e);
    } finally {
      fc.responseComplete();
    }
  }
}
