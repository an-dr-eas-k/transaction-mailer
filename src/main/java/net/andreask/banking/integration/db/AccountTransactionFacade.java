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

package net.andreask.banking.integration.db;

import java.util.List;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;

import net.andreask.banking.integration.db.model.AccountTransactionDE;

/**
 * @author andreask
 */
@NamedQuery(
        name = "findFromTemplate",
        query = "select * from AccountTransactionDE ac where ac.usage = :usage and ac.value = :value"
)
@Singleton
public class AccountTransactionFacade extends AbstractFacade<AccountTransactionDE> {
    @PersistenceContext(unitName = "hv-pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountTransactionFacade() {
        super(AccountTransactionDE.class);
    }

    public List<AccountTransactionDE> find(AccountTransactionDE template) {
        //noinspection unchecked
        return getEntityManager().createNamedQuery("findFromTemplate")
                .setParameter("usage", template.getUsage())
                .setParameter("value", template.getValue())
                .getResultList();

    }

}

