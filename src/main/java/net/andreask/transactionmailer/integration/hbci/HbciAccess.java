package net.andreask.transactionmailer.integration.hbci;

public interface HbciAccess {

  String getBankCode();

  String getHbciVersion();

  String getCustomerId();

  String getAccountNumberStripped();

  String getPin();
}
