package net.andreask.banking.integration.mail;

public interface EMailContent {

  String getSubject();

  String getRecipient();

  String getMessageBody();

}
