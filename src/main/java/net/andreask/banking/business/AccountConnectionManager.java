package net.andreask.banking.business;

import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;

import net.andreask.banking.integration.db.AccountConnectionFacade;

public final class AccountConnectionManager {

    @Inject
    AccountConnectionFacade accountConnectionFacade;

    public void initTimer(TimerService timerService) {
        accountConnectionFacade
                .findAll()
                .stream()
                .map(net.andreask.banking.integration.db.Mapper::map)
                .forEach(ac -> timerService.createCalendarTimer(
                        ac.getScheduleExpression(),
                        new TimerConfig(ac, false)));
    }
}
