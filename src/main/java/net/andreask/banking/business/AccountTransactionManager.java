package net.andreask.banking.business;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.andreask.banking.domain.AccountConnection;
import net.andreask.banking.domain.AccountTransaction;
import net.andreask.banking.integration.db.AccountTransactionFacade;
import net.andreask.banking.integration.hbci.HbciAccess;
import net.andreask.banking.integration.hbci.HbciFacade;
import net.andreask.banking.integration.mail.EMailContent;
import net.andreask.banking.integration.mail.Mailer;

@RequestScoped
public class AccountTransactionManager implements Serializable {

  Logger logger = LogManager.getLogger(AccountTransactionManager.class);

  @Inject
  private AccountTransactionFacade accountTransactionFacade;

  @Inject
  private HbciFacade hbciFacade;

  @Inject
  private Encryptor encryptor;

  @Inject
  private Mailer mailer;

  public void mirrorTransactions(AccountConnection ac) {
    List<AccountTransaction> toNotify = hbciFacade
        .setAccountConnection(toHbciAccess(ac))
        .init()
        .acquireTransactions()
        .stream()
        .filter(e -> accountTransactionFacade.find(e).isEmpty())
        .peek(logger::debug)
        .peek(at -> at.setAccountConnection(ac))
        .peek(accountTransactionFacade::save)
        .collect(Collectors.toList());
    notifyUser(ac.getEmail(), toNotify);

  }

  private void notifyUser(String email, List<AccountTransaction> toNotify) {
    mailer.sendMail(new EMailContent() {

      @Override
      public String getSubject() {
        return String.format("Anzahl der Transaktionen %d", toNotify.size());
      }

      @Override
      public String getRecipient() {
        return email;
      }

      @Override
      public String getMessageBody() {
        return String.format("<html><body>%s</body></html>", toNotify
            .stream()
            .map(ac -> ac.getOther().getName())
            .collect(
                Collectors.joining("<br />")));
      }
    });

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
