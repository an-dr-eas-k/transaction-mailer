package net.andreask.ll.integration.db;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;

import net.andreask.ll.business.AccountConnectionManager;
import net.andreask.ll.domain.AccountConnection;
import net.andreask.ll.domain.AccountTransaction;

/**
 * Created by andreask on 1/18/16.
 */

// @Startup
@Singleton
public class DbToucher {

  @Inject
  private AccountConnectionManager accountConnectionManager;

  @Inject
  private AccountTransactionFacade accountTransactionFacade;

  @Inject
  private AccountConnection ac;

  @PostConstruct
  public void init() {
    LogManager.getLogger(DbToucher.class).info("init");
    accountConnectionManager.save(ac);

    accountTransactionFacade.create(new AccountTransaction());
  }

}
