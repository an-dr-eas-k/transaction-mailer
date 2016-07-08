package net.andreask.transactionmailer.integration.hbci;

public interface HbciAccess {

  String getBankCode();

  String getCustomerId();

  String getAccountNumberStripped();

  String getPin();
}
