package net.andreask.banking.business;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import net.andreask.banking.integration.db.AccountTransactionFacade;
import net.andreask.banking.integration.hbci.HbciFacade;
import net.andreask.banking.model.AccountConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RequestScoped
public class AccountTransactionManager implements Serializable {

    Logger logger = LogManager.getLogger(AccountTransactionManager.class);

    @Inject
    private AccountTransactionFacade accountTransactionFacade;

    @Inject
    private HbciFacade hbciFacade;

    public void mirrorTransactions(AccountConnection ac) {
        hbciFacade
                .setAccountConnection(ac)
                .init()
                .acquireTransactions()
                .stream()
                .map(net.andreask.banking.integration.db.Mapper::map)
                .peek(logger::debug)
                .filter(e -> accountTransactionFacade.find(e) == null)
                .forEach(accountTransactionFacade::create);

    }

}
