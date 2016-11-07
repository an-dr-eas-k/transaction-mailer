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

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import net.andreask.transactionmailer.domain.AccountConnection;
import net.andreask.transactionmailer.domain.AccountTransaction;

/**
 * @author andreask
 */
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class AccountTransactionFacade extends AbstractFacade<AccountTransaction> {
  @PersistenceUnit(unitName = "hv-pu")
  private EntityManagerFactory emf;

  @Override
  protected EntityManager getEntityManager() {
    logger.debug("emf hashcode: " + emf.hashCode());
    EntityManager em = emf.createEntityManager();
    logger.debug("em hashcode: " + em.hashCode());
    return em;
  }

  public AccountTransactionFacade() {
    super(AccountTransaction.class);
    logger.debug(this.getClass().getSimpleName() + " constructed");
  }

  public List<AccountTransaction> find(AccountTransaction template, AccountConnection ac) {
    // noinspection unchecked
    EntityManager em = getEntityManager();
    return em.createNamedQuery("findFromTemplate", AccountTransaction.class)
        .setParameter("usage", template.getUsage())
        .setParameter("value", template.getValue())
        .setParameter("valuta", template.getValuta())
        .setParameter("accountConnection", ac)
        .getResultList()
        .stream()
        .peek(em::refresh)

        .collect(Collectors.toList());

  }

  public void save(AccountTransaction ac) {
    super.create(ac);
  }

  public void save(List<AccountTransaction> ac) {
    ac.stream().forEach(super::create);
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
