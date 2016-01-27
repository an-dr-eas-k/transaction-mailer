package net.andreask.banking.business;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.andreask.banking.integration.db.AccountTransactionFacade;
import net.andreask.banking.integration.hbci.HbciAccess;
import net.andreask.banking.integration.hbci.HbciFacade;
import net.andreask.banking.model.AccountConnection;
import net.andreask.banking.model.AccountTransaction;

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
    List<AccountTransaction> toNotify = hbciFacade
        .setAccountConnection(toHbciAccess(ac))
        .init()
        .acquireTransactions()
        .stream()
        .peek(logger::debug)
        .filter(e -> accountTransactionFacade.find(e) == null)
        .peek(accountTransactionFacade::save)
        .collect(Collectors.toList());
    notifyUser(ac.getEmail(), toNotify);

  }

  private void notifyUser(String email, List<AccountTransaction> toNotify) {
    logger.info("mailing {}, {}", email, toNotify
        .stream()
        .map(ac -> ac.getOther().getName())
        .collect(
            Collectors.joining(",")));
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
