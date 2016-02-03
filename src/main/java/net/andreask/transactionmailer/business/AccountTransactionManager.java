package net.andreask.transactionmailer.business;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.andreask.transactionmailer.domain.AccountConnection;
import net.andreask.transactionmailer.domain.AccountTransaction;
import net.andreask.transactionmailer.integration.db.AccountTransactionFacade;
import net.andreask.transactionmailer.integration.hbci.HbciAccess;
import net.andreask.transactionmailer.integration.hbci.HbciFacade;
import net.andreask.transactionmailer.integration.mail.EMailContent;
import net.andreask.transactionmailer.integration.mail.Mailer;

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
        .peek(logger::debug)
        .filter(e -> accountTransactionFacade.find(e).isEmpty())
        .peek(logger::debug)
        .peek(at -> at.setAccountConnection(ac))
        .peek(accountTransactionFacade::save)
        .collect(Collectors.toList());
    notifyUser(ac.getEmail(), toNotify);

  }

  private void notifyUser(String email, List<AccountTransaction> toNotify) {
    if (toNotify == null || toNotify.isEmpty() || email == null || email.isEmpty()) {
      logger.debug("not sending email");
      return;
    }
    mailer.sendMail(new EMailContent() {

      @Override
      public String getSubject() {
        return String.format("delta %.2f (%d)",
            toNotify
                .stream()
                .mapToDouble(ac -> ((double) ac.getValue() / 100d))
                .sum(),
            toNotify.size());
      }

      @Override
      public String getRecipient() {
        return email;
      }

      @Override
      public String getMessageBody() {
        return String.format(""
            + "<html><body><table>"
            + "<tr><td>Other</td><td>Reason</td><td>amount</td><td>time</td></tr>"
            + "%s"
            + "</table></body></html>", toNotify
                .stream()
                .map(AccountTransactionManager.this::toContent)
                .collect(
                    Collectors.joining("")));
      }
    });
  }

  private String toContent(AccountTransaction at) {
    StringBuilder sb = new StringBuilder();

    sb.append(String.format("<tr><td>%s</td><td>%s</td><td>%.2fEuro</td><td>%s</td></tr>",
        at.getOther().getName(),
        at.getUsage(),
        ((double) at.getValue() / 100d),
        new SimpleDateFormat("hh:mm").format(at.getValuta())));
    return sb.toString();
  }

  public HbciAccess toHbciAccess(AccountConnection ac) {
    return new HbciAccess() {

      @Override
      public String getBankCode() {
        return ac.getBankCode();
      }

      @Override
      public String getHbciVersion() {
        return ac.getHbciVersion();
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

      @Override
      public String toString() {
        return "HbciAccessImpl [getAccountNumberStripped()=" + getAccountNumberStripped()
            + ", getBankCode()=" + getBankCode() + ", getHbciVersion()=" + getHbciVersion() + ", getCustomerId()="
            + getCustomerId() + "]";
      }
    };
  }

}
