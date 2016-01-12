package net.andreask.banking.business;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import net.andreask.banking.integration.db.AccountTransactionFacade;
import net.andreask.banking.integration.hbci.HbciSession;
import net.andreask.banking.model.AccountConnection;

import java.io.Serializable;

@RequestScoped
public class AccountTransactionManager implements Serializable {

    @Inject
    AccountTransactionFacade accountTransactionFacade;

    public void mirrorTransactions(AccountConnection ac) {
        new HbciSession(net.andreask.banking.integration.db.Mapper.map(ac))
                .acquireTransactions()
                .stream()
                .map(net.andreask.banking.integration.db.Mapper::map)
                .filter(e -> accountTransactionFacade.find(e) == null)
                .forEach(accountTransactionFacade::create);

    }


}
