package net.andreask.transactionmailer.integration.mail;

public interface EMailContent {

  String getSubject();

  String getRecipient();

  String getMessageBody();

}
