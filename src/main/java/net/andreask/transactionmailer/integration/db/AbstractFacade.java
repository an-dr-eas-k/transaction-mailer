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

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;

/**
 * @author andreask
 */
public abstract class AbstractFacade<T> {
  private Class<T> entityClass;

  public AbstractFacade(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  protected abstract EntityManager getEntityManager();

  protected boolean create(T entity) {
    try {
      getEntityManager().merge(entity);
    } catch (IllegalArgumentException e) {
      LogManager.getLogger(this.getClass()).info("saving, not merging {}", entity);
      getEntityManager().persist(entity);
    }
    return true;
  }

  public void edit(T entity) {
    getEntityManager().merge(entity);
  }

  public void remove(T entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public T find(int id) {
    return getEntityManager().find(entityClass, id);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected List<T> findAllDE() {
    javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
    cq.select(cq.from(entityClass));
    List<T> result = getEntityManager().createQuery(cq).getResultList();
    return result;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public List<T> findRange(int[] range) {
    javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
    cq.select(cq.from(entityClass));
    javax.persistence.Query q = getEntityManager().createQuery(cq);
    q.setMaxResults(range[1] - range[0]);
    q.setFirstResult(range[0]);
    getEntityManager().clear();
    return q.getResultList();
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public int count() {
    javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
    javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
    cq.select(getEntityManager().getCriteriaBuilder().count(rt));
    javax.persistence.Query q = getEntityManager().createQuery(cq);
    return ((Long) q.getSingleResult()).intValue();
  }

}
