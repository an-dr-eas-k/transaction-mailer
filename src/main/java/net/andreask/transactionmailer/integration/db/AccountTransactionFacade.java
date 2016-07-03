/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 * <p>
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.andreask.transactionmailer.integration.db;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.andreask.transactionmailer.domain.AccountConnection;
import net.andreask.transactionmailer.domain.AccountTransaction;

/**
 * @author andreask
 */
@Singleton
public class AccountTransactionFacade extends AbstractFacade<AccountTransaction> {
  @PersistenceContext(unitName = "hv-pu")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public AccountTransactionFacade() {
    super(AccountTransaction.class);
  }

  public List<AccountTransaction> find(AccountTransaction template, AccountConnection ac) {
    // noinspection unchecked
    return getEntityManager()
        .createNamedQuery("findFromTemplate", AccountTransaction.class)
        .setParameter("usage", template.getUsage())
        .setParameter("value", template.getValue())
        .setParameter("valuta", template.getValuta())
        .setParameter("accountConnection", ac)
        .getResultList()
        .stream()
        .peek(getEntityManager()::refresh)

    .collect(Collectors.toList());

  }

  public void save(AccountTransaction ac) {
    super.create(ac);
  }

  public List<AccountTransaction> findAllTransactions(AccountConnection ac) {
    return getEntityManager()
        .createNamedQuery("findWithAccountConnection", AccountTransaction.class)
        .setParameter("accountConnection", ac)
        .getResultList()
        .stream()
        .peek(getEntityManager()::refresh)

    .collect(Collectors.toList());
    
  }

}
