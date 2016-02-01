package net.andreask.transactionmailer.integration.db;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;

import net.andreask.transactionmailer.business.AccountConnectionManager;
import net.andreask.transactionmailer.domain.AccountConnection;
import net.andreask.transactionmailer.domain.AccountTransaction;

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
