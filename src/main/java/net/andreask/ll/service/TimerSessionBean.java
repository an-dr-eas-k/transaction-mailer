/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 * <p>
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package net.andreask.ll.service;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.andreask.ll.business.AccountConnectionManager;
import net.andreask.ll.business.AccountTransactionManager;
import net.andreask.ll.domain.AccountConnection;

/**
 * TimerBean is a singleton session bean that creates a timer and prints out a
 * message when a timeout occurs.
 */
@Singleton
@Startup
public class TimerSessionBean {

    @Resource
    TimerService timerService;

    @Inject
    AccountConnectionManager accountConnectionManager;
    @Inject
    AccountTransactionManager accountTransactionManager;



    private static final Logger logger =
            LogManager.getLogger(TimerSessionBean.class);

    @Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
    public void resyncConnections() {
        logger.info(String.format("initTimer,  %tT", new Date()));
        accountConnectionManager.initTimerService(this.timerService);

    }

    @Timeout
    public void triggeringTransactionMirror(Timer timer) {
        logger.info(String.format("Programmatic timeout occurred,  %tT", new Date()));
        accountTransactionManager.mirrorTransactions((AccountConnection) timer.getInfo());
    }

}
