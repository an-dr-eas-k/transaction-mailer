package net.andreask.banking.business;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public void initTimer(TimerService timerService) {


        Map<String, Timer> timerAcMap = timerService
                .getAllTimers()
                .stream()
                .filter(timer -> timer.getInfo() != null && timer.getInfo() instanceof AccountConnection)
                .collect(Collectors.toMap(
                        timer -> ((AccountConnection) timer.getInfo()).getGeneratedIban(),
                        timer -> timer));

        accountConnectionFacade
                .findAll()
                .stream()
                .peek(logger::debug)
                .map(net.andreask.banking.integration.db.Mapper::map)
                .collect(Collectors.toList())
                .forEach(
                        ac -> {
                            Timer existingTimer = timerAcMap.get(ac.getGeneratedIban());
                            if (existingTimer != null) {
                                existingTimer.cancel();
                                logger.info("updating schedule for %s: %s (new), %s (old)",
                                        ac.getGeneratedIban(),
                                        ac.getCronScheduleExpression(),
                                        ((AccountConnection) existingTimer.getInfo()).getCronScheduleExpression());
                            } else {
                                logger.info("found new account %s, schedule: %s",
                                        ac.getGeneratedIban(),
                                        ac.getCronScheduleExpression());

                            }
                            timerService.createCalendarTimer(
                                    ac.getScheduleExpression(),
                                    new TimerConfig(ac, false));

                        });
    }
}
