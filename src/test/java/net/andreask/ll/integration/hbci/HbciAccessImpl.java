
package net.andreask.ll.integration.hbci;

import javax.ejb.ScheduleExpression;

import net.andreask.ll.business.Configuration;
import net.andreask.ll.business.Encryptor;
import net.andreask.ll.domain.AccountConnection;
import net.andreask.ll.integration.hbci.HbciAccess;

public class HbciAccessImpl implements HbciAccess {

  Configuration c = new Configuration();

  private AccountConnection ac = new AccountConnection();

  @Override
  public String getPin() {
    return Encryptor.decrypt(ac.getEncryptedPin(), c.produceKey(), c.produceAlgorithmParameterSpec());
  }

  public ScheduleExpression getScheduleExpression() {
    return ac.getScheduleExpression();
  }

  public String getGeneratedIban() {
    return ac.getGeneratedIban();
  }

  public int getId() {
    return ac.getId();
  }

  public HbciAccessImpl setId(int id) {
    ac.setId(id);
    return this;
  }

  public String getEncryptedPin() {
    return ac.getEncryptedPin();
  }

  public HbciAccessImpl setEncryptedPin(String pin) {
    ac.setEncryptedPin(pin);
    return this;
  }

  public int hashCode() {
    return ac.hashCode();
  }

  public String getUrl() {
    return ac.getUrl();
  }

  public HbciAccessImpl setUrl(String url) {
    ac.setUrl(url);
    return this;
  }

  public String getAccountNumber10() {
    return ac.getAccountNumber10();
  }

  public String getAccountNumberStripped() {
    return ac.getAccountNumberStripped();
  }

  public HbciAccessImpl setAccountNumber(String accountNumber) {
    ac.setAccountNumberStripped(accountNumber);
    return this;
  }

  public String getBankCode() {
    return ac.getBankCode();
  }

  public HbciAccessImpl setBankCode(String bankCode) {
    ac.setBankCode(bankCode);
    return this;
  }

  public String getHbciVersion() {
    return ac.getHbciVersion();
  }

  public HbciAccessImpl setHbciVersion(String hbciVersion) {
    ac.setHbciVersion(hbciVersion);
    return this;
  }

  public String getCronScheduleExpression() {
    return ac.getCronScheduleExpression();
  }

  public HbciAccessImpl setCronScheduleExpression(String cronScheduleExpression) {
    ac.setCronScheduleExpression(cronScheduleExpression);
    return this;
  }

  public String getCountryCode() {
    return ac.getCountryCode();
  }

  public HbciAccessImpl setCountryCode(String countryCode) {
    ac.setCountryCode(countryCode);
    return this;
  }

  public String toString() {
    return ac.toString();
  }

  public boolean equals(Object obj) {
    return ac.equals(obj);
  }

  public HbciAccessImpl setCustomerId(String customerId) {
    ac.setCustomerId(customerId);
    return this;
  }

  public String getCustomerId() {
    return ac.getCustomerId();
  }

  public String getEmail() {
    return ac.getEmail();
  }

  public HbciAccessImpl setEmail(String email) {
    ac.setEmail(email);
    return this;
  }

  public HbciAccessImpl setPin(String readLine) {
    ac.setEncryptedPin(Encryptor.encrypt(readLine, c.produceKey(), c.produceAlgorithmParameterSpec()));
    return this;

  }

}
