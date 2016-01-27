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
import java.util.stream.Collectors;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.andreask.banking.integration.db.model.AccountConnectionDE;
import net.andreask.banking.model.AccountConnection;

/**
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

    public List<AccountConnection> findAll() {

        return super
                .findAllDE()
                .stream()
                .peek(getEntityManager()::refresh)
                .map(MapperDE::map)
                .collect(Collectors.toList());
    }

    public void save(AccountConnection ac) {
        super.create(MapperDE.map(ac));
    }
}

