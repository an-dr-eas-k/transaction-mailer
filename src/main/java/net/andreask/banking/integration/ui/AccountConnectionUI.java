/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 * <p>
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package net.andreask.banking.integration.ui;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import net.andreask.banking.business.AccountConnectionManager;
import net.andreask.banking.domain.AccountConnection;

/**
 * @author ian
 */
@ManagedBean
@RequestScoped
public class AccountConnectionUI {

  @Inject
  private AccountConnectionManager accountConnectionManager;

  @Inject
  private AccountConnection accountConnection;

  public AccountConnection getAccountConnection() {
    return this.accountConnection;
  }

  public List<AccountConnection> getAllAccountConnections() {
    return accountConnectionManager
        .getAllAccountConnections();
  }

  public String save() {
    this.accountConnectionManager.save(accountConnection);
    return null;
  }

}
