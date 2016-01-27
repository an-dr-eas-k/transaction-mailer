package net.andreask.banking.integration.ui.model;

import javax.enterprise.inject.Model;

/**
 * Die AccountConnection als User Interface Model
 * 
 * @author aki
 *
 */
@Model
public class AccountConnectionUIM {
  private String pin;
  private String url;

  private String accountNumber; // kontonummer
  private String bankCode; // blz
  private String hbciVersion;
  private String cronScheduleExpression;

  private String countryCode = "DE"; // DE
  private String customerId; // login username
  private int id;
  private String email;

  public String getPin() {
    return pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getBankCode() {
    return bankCode;
  }

  public void setBankCode(String bankCode) {
    this.bankCode = bankCode;
  }

  public String getHbciVersion() {
    return hbciVersion;
  }

  public void setHbciVersion(String hbciVersion) {
    this.hbciVersion = hbciVersion;
  }

  public String getCronScheduleExpression() {
    return cronScheduleExpression;
  }

  public void setCronScheduleExpression(String cronScheduleExpression) {
    this.cronScheduleExpression = cronScheduleExpression;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return this.id;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

}
