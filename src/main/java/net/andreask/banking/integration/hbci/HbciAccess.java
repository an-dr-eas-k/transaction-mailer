package net.andreask.banking.integration.hbci;

public interface HbciAccess {

    String getUrl();

    String getBankCode();

    String getHbciVersion();

    String getCountryCode();

    String getCustomerId();

    String getAccountNumberStripped();

    String getPin();
}
