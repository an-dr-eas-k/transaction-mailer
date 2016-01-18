package net.andreask.banking.integration.hbci.hbci4java;

import net.andreask.banking.integration.hbci.HbciFacade;
import net.andreask.banking.model.AccountConnection;
import net.andreask.banking.model.AccountTransaction;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class HbciFacadeImpl implements HbciFacade {

    private AccountConnection accountConnection;

    private HbciSession hbciSession;

    public AccountConnection getAccountConnection() {
        return accountConnection;
    }

    @Override
    public HbciFacade setAccountConnection(AccountConnection accountConnection) {
        this.accountConnection = accountConnection;
        return this;
    }

    @Override
    public HbciFacade init() {
        hbciSession = new HbciSession(this.accountConnection);
        hbciSession.initSession();
        return this;
    }

    @Override
    public List<AccountTransaction> acquireTransactions() {
        return this.hbciSession.acquireTransactions();
    }

    @Override
    public long acquireBalance() {
        return this.hbciSession.acquireBalance();
    }
}
