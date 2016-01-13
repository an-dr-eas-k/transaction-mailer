package net.andreask.banking.business;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import net.andreask.banking.integration.db.AccountTransactionFacade;
import net.andreask.banking.integration.hbci.HbciSession;
import net.andreask.banking.model.AccountConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RequestScoped
public class AccountTransactionManager implements Serializable {

    Logger logger = LogManager.getLogger(AccountTransactionManager.class);

    @Inject
    AccountTransactionFacade accountTransactionFacade;

    public void mirrorTransactions(AccountConnection ac) {
        new HbciSession(net.andreask.banking.integration.db.Mapper.map(ac))
                .acquireTransactions()
                .stream()
                .peek(logger::debug)
                .map(net.andreask.banking.integration.db.Mapper::map)
                .filter(e -> accountTransactionFacade.find(e) == null)
                .forEach(accountTransactionFacade::create);

    }


}
