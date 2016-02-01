package net.andreask.transactionmailer.integration.hbci;

import java.util.List;

import net.andreask.transactionmailer.domain.AccountTransaction;

/**
 * Created by andreask on 1/18/16.
 */
public interface HbciFacade {
    HbciFacade setAccountConnection(HbciAccess accountConnection);

    HbciFacade init();

    List<AccountTransaction> acquireTransactions();

    long acquireBalance();
}
