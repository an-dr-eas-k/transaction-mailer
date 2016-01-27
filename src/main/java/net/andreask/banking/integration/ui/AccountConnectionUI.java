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
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import net.andreask.banking.business.AccountConnectionManager;
import net.andreask.banking.business.Encryptor;
import net.andreask.banking.integration.ui.model.AccountConnectionUIM;
import net.andreask.banking.model.AccountConnection;

/**
 * @author ian
 */
@ManagedBean
@RequestScoped
public class AccountConnectionUI implements Serializable {

  @Inject
  private AccountConnectionManager accountConnectionManager;

  @Inject
  private AccountConnectionUIM accountConnectionUIM;

  @Inject
  private Encryptor encryptor;

  /**
   * Creates a new instance of AccountConnectionUI
   */
  public AccountConnectionUI() {

  }

  public List<AccountConnectionUIM> getAllAccountConnections() {
    return accountConnectionManager
        .queryAccountConnections()
        .stream()
        .map(ac -> {
          AccountConnectionUIM uim = MapperUI.toUIM(ac);
          uim.setPin(encryptor.decrypt(ac.getEncryptedPin()));
          return uim;
        })
        .collect(Collectors.toList());
  }

  public String save() {
    AccountConnection ac = MapperUI.fromUIM(this.accountConnectionUIM);
    ac.setEncryptedPin(encryptor.encrypt(this.accountConnectionUIM.getPin()));
    this.accountConnectionManager.save(ac);
    return null;
  }

}
