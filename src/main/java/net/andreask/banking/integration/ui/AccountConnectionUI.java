/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package net.andreask.banking.integration.ui;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.andreask.banking.business.AccountConnectionManager;
import net.andreask.banking.service.TimerSessionBean;

/**
 *
 * @author ian
 */
@Named
@SessionScoped
public class AccountConnectionUI implements Serializable {

    @Inject
    private AccountConnectionManager accountConnectionManager;



    /** Creates a new instance of AccountConnectionUI */
    public AccountConnectionUI() {

    }




}
