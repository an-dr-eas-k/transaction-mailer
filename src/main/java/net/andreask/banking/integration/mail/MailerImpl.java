package net.andreask.banking.integration.mail;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

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

      // Body text.
      BodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setText(mailContent.getMessageBody());

      // Multipart message.
      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(messageBodyPart);

      // Add multipart message to email.
      msg.setContent(multipart);

      // Send email.
      Transport.send(msg);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}