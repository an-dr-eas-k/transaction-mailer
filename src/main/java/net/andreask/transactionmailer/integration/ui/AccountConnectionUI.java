/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 * <p>
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package net.andreask.transactionmailer.integration.ui;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import net.andreask.transactionmailer.business.AccountConnectionManager;
import net.andreask.transactionmailer.domain.AccountConnection;

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

  @PostConstruct
  public void init() {
    String deleteId = ((javax.servlet.http.HttpServletRequest) javax.faces.context.FacesContext.getCurrentInstance()
        .getExternalContext().getRequest()).getParameter("deleteId");
    if (deleteId != null) {
      this.accountConnectionManager.delete(Integer.parseInt(deleteId));
    }

  }

  public AccountConnection getAccountConnection() {
    String editId = ((javax.servlet.http.HttpServletRequest) javax.faces.context.FacesContext.getCurrentInstance()
        .getExternalContext().getRequest()).getParameter("editId");
    if (editId != null) {
      return this.accountConnectionManager.queryFromId(Integer.parseInt(editId));
    }
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

  public String editAction(AccountConnection ac) {
    ac.setEditable(true);
    return null;
  }

}
