package net.andreask.banking.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import net.andreask.banking.integration.db.AccountConnectionFacade;
import net.andreask.banking.model.AccountConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RequestScoped
public class AccountConnectionManager implements Serializable {

    Logger logger = LogManager.getLogger(AccountTransactionManager.class);

    @Inject
    AccountConnectionFacade accountConnectionFacade;

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
                //                .peek(ac -> logger.debug("found accound in database {}", ac))
                .forEach(
                        ac -> {
                            Timer existingTimer = timerAcMap.get(ac.getGeneratedIban());
                            boolean createSchedule = false;
                            if (existingTimer == null) {
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
                        });
        return validAccounts;
    }

    private void cancelRemovedTimers(TimerService timerService, List<String> validAccounts) {
        createLocalActiveTimerStream(timerService)
                .filter(timer -> !validAccounts.contains(((AccountConnection) timer.getInfo()).getGeneratedIban()))
                .peek(timer -> logger.debug("canceling timer for {}", timer.getInfo()))
                .forEach(Timer::cancel);
    }

    public List<AccountConnection> queryAccountConnections() {
        return this.accountConnectionFacade.findAll();
    }

}
