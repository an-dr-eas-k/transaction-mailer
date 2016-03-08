package net.andreask.transactionmailer.integration.hbci.hbci4java;

import java.util.List;

import javax.enterprise.context.RequestScoped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.andreask.transactionmailer.domain.AccountTransaction;
import net.andreask.transactionmailer.integration.hbci.HbciAccess;
import net.andreask.transactionmailer.integration.hbci.HbciFacade;

@RequestScoped
public class HbciFacadeImpl implements HbciFacade {

	private HbciAccess hbciAccess;

	private HbciSession hbciSession;
	static Logger logger = LogManager.getLogger(HbciFacadeImpl.class);

	@Override
	public HbciFacade setAccountConnection(HbciAccess accountConnection) {
		this.hbciAccess = accountConnection;
		return this;
	}

	@Override
	public HbciFacade init() {
		logger.info(this.hbciAccess);
		hbciSession = new HbciSession(this.hbciAccess);
		hbciSession.initSession();
		return this;
	}

	@Override
	public List<AccountTransaction> acquireTransactions() {
		List<AccountTransaction> result = this.hbciSession.acquireTransactions();
		logger.info("found {} transactions", result.size());
		return result;
	}

	@Override
	public long acquireBalance() {
		return this.hbciSession.acquireBalance();
	}

	@Override
	public void close() {
		hbciSession.close();
		hbciSession = null;
	}
}
