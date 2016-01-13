package net.andreask.banking.business;

import java.io.Serializable;

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

    public void initTimer(TimerService timerService) {

        accountConnectionFacade
                .findAll()
                .stream()
                .peek(logger::debug)
                .map(net.andreask.banking.integration.db.Mapper::map)
                .filter(accountConnection ->
                        !timerService
                                .getAllTimers()
                                .stream()
                                .filter(timer -> timer.getInfo() != null)
                                .map(timer -> (AccountConnection) timer.getInfo())
                                .filter(timerConnection -> timerConnection.getBankCode().equals(accountConnection
                                        .getBankCode()) && timerConnection.getAccountNumber().equals
                                        (accountConnection.getAccountNumber()))
                                .allMatch(timerConnection -> timerConnection.getCronScheduleExpression().equals
                                        (accountConnection.getCronScheduleExpression())))

                .peek(a -> logger.debug(String.format("creating timer for %s", a.toString())))
                .forEach(ac -> {
                    try {
                        timerService.createCalendarTimer(
                                ac.getScheduleExpression(),
                                new TimerConfig(ac, false));
                    } catch (Exception ae) {
                        logger.warn("cannot interprete schedule expression",
                                ac.getCronScheduleExpression());
                    }
                });
    }
}
