package net.andreask.transactionmailer.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;

import javax.ejb.ScheduleExpression;
import javax.inject.Inject;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.iban4j.CountryCode;
import org.iban4j.Iban;

import net.andreask.transactionmailer.business.Encryptor;

@Entity

public class AccountConnection implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Inject
  @Transient
  private Encryptor encryptor;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  private String title;

  private String encryptedPin;

  private String accountNumber; // kontonummer
  private String bankCode; // blz

  private String cronScheduleExpression;

  private String customerId; // login username

  private String email;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "accountConnection")
  protected Set<AccountTransaction> transactions;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ScheduleExpression getScheduleExpression() {
    String[] scheduleValues = this.cronScheduleExpression.split(" +");
    if (scheduleValues.length != 3) {
      throw new IllegalArgumentException("cron expression not valid: " + this.cronScheduleExpression);
    }

    return new ScheduleExpression().dayOfWeek(scheduleValues[2]).hour(scheduleValues[1]).minute(scheduleValues[0]);

  }

  public String getGeneratedIban() {
    return new Iban.Builder().bankCode(getBankCode()).accountNumber(getAccountNumber10())
        .countryCode(CountryCode.getByCode("DE")).build().toString();
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
    if (encryptor != null) {
      return encryptor.decrypt(this.getEncryptedPin());
    }
    return null;
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

  public String getAccountNumber() {
    return accountNumber;
  }

  public String getBankCode() {
    return bankCode;
  }

  public void setBankCode(String bankCode) {
    this.bankCode = bankCode;
  }

  public String getCronScheduleExpression() {
    return cronScheduleExpression;
  }

  public void setCronScheduleExpression(String cronScheduleExpression) {
    this.cronScheduleExpression = cronScheduleExpression;
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

  public Set<AccountTransaction> getTransactions() {
    return transactions;
  }

  public void setTransactions(Set<AccountTransaction> transactions) {
    this.transactions = transactions;
  }

  public String toCSV() {
    return String.join(", ", Arrays.asList(new String[] {
        Long.toString(id),
        customerId,
        email,
        getGeneratedIban() }));
  }

  static public String toCSVHeader() {
    return String.join(", ", Arrays.asList(new String[] {
        "id",
        "customerId",
        "email",
        "GeneratedIban" }));
  }

}
