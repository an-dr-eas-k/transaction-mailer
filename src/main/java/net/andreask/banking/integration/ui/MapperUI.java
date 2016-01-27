package net.andreask.banking.integration.ui;

import net.andreask.banking.integration.ui.model.AccountConnectionUIM;
import net.andreask.banking.model.AccountConnection;

public class MapperUI {

  public static AccountConnection fromUIM(AccountConnectionUIM uim) {

    return new AccountConnection()
        .setId(uim.getId())
        .setAccountNumberStripped(uim.getAccountNumber())
        .setBankCode(uim.getBankCode())
        .setCustomerId(uim.getCustomerId())
        .setHbciVersion(uim.getHbciVersion())
        .setEmail(uim.getEmail())
        .setUrl(uim.getUrl())
        .setCronScheduleExpression(uim.getCronScheduleExpression());
  }

  public static AccountConnectionUIM toUIM(AccountConnection ac) {

    AccountConnectionUIM uim = new AccountConnectionUIM();
    uim.setId(ac.getId());
    uim.setAccountNumber(ac.getAccountNumberStripped());
    uim.setBankCode(ac.getBankCode());
    uim.setCustomerId(ac.getCustomerId());
    uim.setHbciVersion(ac.getHbciVersion());
    uim.setEmail(ac.getEmail());
    uim.setUrl(ac.getUrl());
    uim.setCronScheduleExpression(ac.getCronScheduleExpression());
    return uim;
  }
}
