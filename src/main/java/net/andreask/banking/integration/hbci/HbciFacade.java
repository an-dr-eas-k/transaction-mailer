package net.andreask.banking.integration.hbci;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import net.andreask.banking.integration.hbci.hbci4java.HbciSession;
import net.andreask.banking.model.AccountConnection;
import net.andreask.banking.model.AccountTransaction;

@Named
@RequestScoped
public class HbciFacade {

    private AccountConnection accountConnection;

    private HbciSession hbciSession;

    public AccountConnection getAccountConnection() {
        return accountConnection;
    }

    public HbciFacade setAccountConnection(AccountConnection accountConnection) {
        this.accountConnection = accountConnection;
        return this;
    }

    public HbciFacade init() {
        hbciSession = new HbciSession(this.accountConnection);
        return this;
    }

    public List<AccountTransaction> acquireTransactions() {
        return this.hbciSession.acquireTransactions();
    }

    public long acquireBalance() {
        return this.hbciSession.acquireBalance();
    }
}
