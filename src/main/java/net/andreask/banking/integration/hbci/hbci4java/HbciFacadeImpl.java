package net.andreask.banking.integration.hbci.hbci4java;

import java.util.List;

import javax.enterprise.context.RequestScoped;

import net.andreask.banking.integration.hbci.HbciAccess;
import net.andreask.banking.integration.hbci.HbciFacade;
import net.andreask.banking.model.AccountTransaction;

@RequestScoped
public class HbciFacadeImpl implements HbciFacade {

  private HbciAccess accountConnection;

  private HbciSession hbciSession;

  @Override
  public HbciFacade setAccountConnection(HbciAccess accountConnection) {
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
