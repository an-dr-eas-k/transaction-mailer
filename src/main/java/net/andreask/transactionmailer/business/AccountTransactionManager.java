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

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

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
    try {
      List<AccountTransaction> toNotify = hbciFacade
          .setAccountConnection(toHbciAccess(ac))
          .init()
          .acquireTransactions()
          .stream()
          .filter(e -> accountTransactionFacade.find(e, ac).isEmpty())
          .peek(logger::debug)
          .peek(at -> at.setAccountConnection(ac))
          // .peek(accountTransactionFacade::save)
          .collect(Collectors.toList());
      notifyUser(ac, toNotify);
      accountTransactionFacade.save(toNotify);
    } finally {
      hbciFacade.close();
    }
  }

  private void notifyUser(AccountConnection ac, List<AccountTransaction> toNotify) {
    if (toNotify == null || toNotify.isEmpty() || ac.getEmail() == null || ac.getEmail().isEmpty()) {
      logger.debug("not sending email, reason {}, {}, {}, {}",
          toNotify == null,
          toNotify.isEmpty(),
          ac.getEmail() == null,
          ac.getEmail().isEmpty());
      return;
    }
    mailer.sendMail(new EMailContent() {

      @Override
      public String getSubject() {
	return String.format("%s: delta %.2f (%d)",
	    ac.getTitle(),
	    toNotify
	        .stream()
	        .mapToDouble(ac -> ((double) ac.getValue() / 100d))
	        .sum(),
	    toNotify.size());
      }

      @Override
      public String getRecipient() {
	return ac.getEmail();
      }

      @Override
      public String getMessageBody() {
	return String.format(""
	    + "<html><body><table>"
	    + "<tr><td>Other</td><td>Reason</td><td>amount</td><td>time</td></tr>"
	    + "%s"
	    + "</table></body></html>", toNotify
	        .stream()
	        .sorted((a, b) -> -1 * a.getValuta().compareTo(b.getValuta()))
	        .map(AccountTransactionManager.this::toContent)
	        .collect(
	            Collectors.joining("")));
      }
    });
  }

  private String toContent(AccountTransaction at) {
    if (at == null) {
      return "";
    }
    StringBuilder sb = new StringBuilder();

    sb.append(String.format("<tr><td>%s</td><td>%s</td><td>%.2fEuro</td><td>%s</td></tr>",
        at.getOther() != null ? at.getOther().getName() : "",
        at.getUsage(),
        ((double) at.getValue() / 100d),
        new SimpleDateFormat("yyyy-MM-dd_hh:mm").format(at.getValuta())));
    return sb.toString();
  }

  public HbciAccess toHbciAccess(AccountConnection ac) {
    return new HbciAccess() {

      @Override
      public String getBankCode() {
	return ac.getBankCode();
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
	    + ", getBankCode()=" + getBankCode() + ", getCustomerId()="
	    + getCustomerId() + "]";
      }
    };
  }

}
