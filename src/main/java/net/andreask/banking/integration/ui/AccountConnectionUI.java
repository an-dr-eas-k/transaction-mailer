/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 * <p>
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package net.andreask.banking.integration.ui;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.andreask.banking.business.AccountConnectionManager;
import net.andreask.banking.model.AccountConnection;

/**
 * @author ian
 */
@Named
@SessionScoped
public class AccountConnectionUI implements Serializable {

    @Inject
    private AccountConnectionManager accountConnectionManager;

    /**
     * Creates a new instance of AccountConnectionUI
     */
    public AccountConnectionUI() {

    }

    public List<AccountConnection> getAllAccountConnections() {
        return accountConnectionManager.queryAccountConnections();
    }

    public void save(AccountConnection ac) {
        this.accountConnectionManager.save(ac);
    }

}
