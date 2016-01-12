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

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.andreask.banking.integration.db.model.AccountConnectionDE;

/**
 *
 * @author andreask
 */
@Singleton
public class AccountConnectionFacade extends AbstractFacade<AccountConnectionDE> {
    @PersistenceContext(unitName = "hv-pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountConnectionFacade() {
        super(AccountConnectionDE.class);
    }

}

