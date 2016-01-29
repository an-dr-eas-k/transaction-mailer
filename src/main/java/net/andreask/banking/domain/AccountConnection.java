package net.andreask.banking.domain;

import java.io.Serializable;

import javax.ejb.ScheduleExpression;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.iban4j.CountryCode;
import org.iban4j.Iban;

import net.andreask.banking.business.Encryptor;

@Entity

public class AccountConnection implements Serializable {

  @Inject
  @Transient
  private Encryptor encryptor;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
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

  public void setAccountNumberStripped(String accountNumber) {
    this.accountNumber = String.format("%10s", accountNumber).replace(' ', '0');
  }

  public void setAccountNumber10(String accountNumber) {
    if (accountNumber.length() != 10) {
      throw new IllegalArgumentException(accountNumber);
    }
    this.accountNumber = accountNumber;
  }

  public String getPin() {
    return encryptor.decrypt(this.getEncryptedPin());
  }

  public void setPin(String pin) {
    this.setEncryptedPin(encryptor.encrypt(pin));
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEncryptedPin() {
    return encryptedPin;
  }

  public void setEncryptedPin(String encryptedPin) {
    this.encryptedPin = encryptedPin;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return "AccountConnection [encryptor=" + encryptor + ", id=" + id + ", encryptedPin=" + encryptedPin + ", url="
        + url + ", accountNumber=" + accountNumber + ", bankCode=" + bankCode + ", hbciVersion=" + hbciVersion
        + ", cronScheduleExpression=" + cronScheduleExpression + ", countryCode=" + countryCode + ", customerId="
        + customerId + ", email=" + email + "]";
  }

}
