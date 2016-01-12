package net.andreask.banking.business;

import javax.annotation.Resource;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import net.andreask.banking.integration.db.AccountConnectionFacade;

import java.io.Serializable;

@RequestScoped
public class AccountConnectionManager implements Serializable {

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
