package net.andreask.banking.business;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.andreask.banking.integration.db.AccountTransactionFacade;
import net.andreask.banking.integration.hbci.HbciAccess;
import net.andreask.banking.integration.hbci.HbciFacade;
import net.andreask.banking.model.AccountConnection;

@RequestScoped
public class AccountTransactionManager implements Serializable {

    Logger logger = LogManager.getLogger(AccountTransactionManager.class);

    @Inject
    private AccountTransactionFacade accountTransactionFacade;

    @Inject
    private HbciFacade hbciFacade;

    @Inject
    private Encryptor encryptor;

    public void mirrorTransactions(AccountConnection ac) {
        hbciFacade
                .setAccountConnection(toHbciAccess(ac))
                .init()
                .acquireTransactions()
                .stream()
                .peek(logger::debug)
                .filter(e -> accountTransactionFacade.find(e) == null)
                .forEach(accountTransactionFacade::save);

    }

    public HbciAccess toHbciAccess(AccountConnection ac) {
        return new HbciAccess() {
            @Override
            public String getUrl() {
                return ac.getUrl();
            }

            @Override
            public String getBankCode() {
                return ac.getBankCode();
            }

            @Override
            public String getHbciVersion() {
                return ac.getHbciVersion();
            }

            @Override
            public String getCountryCode() {
                return ac.getCountryCode();
            }

            @Override
            public String getCustomerId() {
                return ac.getCustomerId();
            }

            @Override
            public String getAccountNumberStripped() {
                return ac.getAccountNumberStripped();
            }

            @Override
            public String getPin() {
                return encryptor.decrypt(ac.getEncryptedPin());
            }
        };
    }

}
