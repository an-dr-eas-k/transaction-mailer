package net.andreask.transactionmailer.integration.mail;

import java.util.Date;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

@RequestScoped
public class MailerImpl implements Mailer {

  @Resource(name = "mail/hv")
  private Session session;

  public void sendMail(EMailContent mailContent) {
    try {
      // InitialContext ctx = new InitialContext();
      // Session session = (Session) ctx.lookup("mail/<name>");

      // Create email and headers.
      Message msg = new MimeMessage(session);
      msg.setSubject(mailContent.getSubject());
      msg.setRecipient(RecipientType.TO,
          new InternetAddress(mailContent.getRecipient()));
      msg.setFrom();
      msg.setSentDate(new Date());

      // Body text.

      msg.setContent(mailContent.getMessageBody(), "text/html; charset=utf-8");

      // Send email.
      Transport.send(msg);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}