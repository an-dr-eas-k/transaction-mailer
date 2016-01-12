/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 * <p>
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package net.andreask.banking.service;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.Init;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.inject.Inject;

import net.andreask.banking.business.AccountConnectionManager;
import net.andreask.banking.business.AccountTransactionManager;
import net.andreask.banking.model.AccountConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private Date lastProgrammaticTimeout;
    private Date lastAutomaticTimeout;

    private static final Logger logger =
            LogManager.getLogger(TimerSessionBean.class);

    @Init
    public void initTimer() {
        logger.info("initTimer");
        accountConnectionManager.initTimer(this.timerService);

    }

    @Timeout
    public void programmaticTimeout(Timer timer) {
        logger.info("Programmatic timeout occurred.");
        accountTransactionManager.mirrorTransactions((AccountConnection) timer.getInfo());
    }

}
