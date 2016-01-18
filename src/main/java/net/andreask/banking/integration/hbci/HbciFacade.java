package net.andreask.banking.integration.hbci;

import net.andreask.banking.model.AccountConnection;
import net.andreask.banking.model.AccountTransaction;

import java.util.List;

/**
 * Created by andreask on 1/18/16.
 */
public interface HbciFacade {
    HbciFacade setAccountConnection(AccountConnection accountConnection);

    HbciFacade init();

    List<AccountTransaction> acquireTransactions();

    long acquireBalance();
}
