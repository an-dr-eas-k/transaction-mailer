package net.andreask.banking.integration.hbci;

import java.util.List;

import net.andreask.banking.model.AccountTransaction;

/**
 * Created by andreask on 1/18/16.
 */
public interface HbciFacade {
    HbciFacade setAccountConnection(HbciAccess accountConnection);

    HbciFacade init();

    List<AccountTransaction> acquireTransactions();

    long acquireBalance();
}
