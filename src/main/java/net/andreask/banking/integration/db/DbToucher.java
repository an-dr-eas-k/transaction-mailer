package net.andreask.banking.integration.db;

import net.andreask.banking.integration.db.model.AccountConnectionDE;
import net.andreask.banking.integration.db.model.AccountTransactionDE;
import org.apache.logging.log4j.LogManager;

import javax.annotation.PostConstruct;
import javax.ejb.Init;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by andreask on 1/18/16.
 */
@Startup
@Singleton
public class DbToucher {

    @Inject
    private AccountConnectionFacade accountConnectionFacade;

    @Inject
    private AccountTransactionFacade accountTransactionFacade;

    @PostConstruct
    public void init() {
        LogManager.getLogger(DbToucher.class).info("init");
        accountConnectionFacade.create(new AccountConnectionDE());
        accountTransactionFacade.create(new AccountTransactionDE());
    }

}
