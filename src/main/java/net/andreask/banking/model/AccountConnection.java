package net.andreask.banking.model;

import java.io.Serializable;

import javax.ejb.ScheduleExpression;

import org.iban4j.CountryCode;
import org.iban4j.Iban;

public class AccountConnection implements Serializable {
  private int id;

  private String encryptedPin;
  private String url;

  private String accountNumber; // kontonummer
  private String bankCode; // blz
  private String hbciVersion;
  private String cronScheduleExpression;

  private String countryCode = "DE"; // DE
  private String customerId; // login username

  private String email;

  public ScheduleExpression getScheduleExpression() {
    String[] scheduleValues = this.cronScheduleExpression.split(" +");
    if (scheduleValues.length != 3) {
      throw new IllegalArgumentException("cron expression not valid: " + this.cronScheduleExpression);
    }

    return new ScheduleExpression().dayOfWeek(scheduleValues[2]).hour(scheduleValues[1]).minute(scheduleValues[0]);

  }

  public String getGeneratedIban() {
    return new Iban.Builder().bankCode(getBankCode()).accountNumber(getAccountNumber10())
        .countryCode(CountryCode.getByCode(getCountryCode())).build().toString();
  }

  public int getId() {
    return this.id;
  }

  public AccountConnection setId(int id) {
    this.id = id;
    return this;
  }

  public String getEncryptedPin() {
    return this.encryptedPin;
  }

  public AccountConnection setEncryptedPin(String pin) {
    this.encryptedPin = pin;
    return this;
  }

  public String getUrl() {
    return this.url;
  }

  public AccountConnection setUrl(String url) {
    this.url = url;
    return this;
  }

  public String getAccountNumber10() {
    return this.accountNumber;
  }

  public String getAccountNumberStripped() {
    if (this.accountNumber != null) {
      return this.accountNumber.replaceAll("^0+", "");
    } else {
      return null;
    }
  }

  public AccountConnection setAccountNumberStripped(String accountNumber) {
    this.accountNumber = String.format("%10s", accountNumber).replace(' ', '0');
    return this;
  }

  public AccountConnection setAccountNumber10(String accountNumber) {
    if (accountNumber.length() != 10) {
      throw new IllegalArgumentException(accountNumber);
    }
    this.accountNumber = accountNumber;
    return this;
  }

  public String getBankCode() {
    return this.bankCode;
  }

  public AccountConnection setBankCode(String bankCode) {
    this.bankCode = bankCode;
    return this;
  }

  public String getHbciVersion() {
    return this.hbciVersion;
  }

  public AccountConnection setHbciVersion(String hbciVersion) {
    this.hbciVersion = hbciVersion;
    return this;
  }

  public String getCronScheduleExpression() {
    return this.cronScheduleExpression;
  }

  public AccountConnection setCronScheduleExpression(String cronScheduleExpression) {
    this.cronScheduleExpression = cronScheduleExpression;
    return this;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public AccountConnection setCountryCode(String countryCode) {
    this.countryCode = countryCode;
    return this;
  }

  @Override
  public String toString() {
    return "AccountConnection{" + "id=" + id + ", url='" + url + '\'' + ", accountNumber='" + accountNumber + '\''
        + ", bankCode='" + bankCode + '\'' + ", hbciVersion='" + hbciVersion + '\''
        + ", cronScheduleExpression='" + cronScheduleExpression + '\'' + ", countryCode='" + countryCode + '\''
        + '}';
  }

  public AccountConnection setCustomerId(String customerId) {
    this.customerId = customerId;
    return this;
  }

  public String getCustomerId() {
    return customerId;
  }

  public String getEmail() {
    return email;
  }

  public AccountConnection setEmail(String email) {
    this.email = email;
    return this;
  }

}
