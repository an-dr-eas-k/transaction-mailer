package net.andreask.banking.model;

import java.io.Serializable;

import javax.ejb.ScheduleExpression;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.iban4j.CountryCode;
import org.iban4j.Iban;

@ManagedBean
@RequestScoped
public class AccountConnection <T extends AccountConnection> implements Serializable{
    private int id;

    private String encryptedPin;
    private String url;

    private String accountNumber; //kontonummer
    private String bankCode; // blz
    private String hbciVersion;
    private String cronScheduleExpression;

    private String countryCode = "DE"; // DE
    private String customerId; // login username


    public ScheduleExpression getScheduleExpression() {
        String[] scheduleValues = this.cronScheduleExpression.split(" +");
        if (scheduleValues.length != 3) {
            throw new IllegalArgumentException("cron expression not valid: " + this.cronScheduleExpression);
        }

        return new ScheduleExpression()
                .dayOfWeek(scheduleValues[2])
                .hour(scheduleValues[1])
                .minute(scheduleValues[0]);

    }

    public String getGeneratedIban() {
        return new Iban.Builder()
                .bankCode(getBankCode())
                .accountNumber(getAccountNumber10())
                .countryCode(CountryCode.getByCode(getCountryCode()))
                .build()
                .toString();
    }

    public int getId() {
        return this.id;
    }

    public T setId(int id) {
        this.id = id;
        return (T)this;
    }


    public String getEncryptedPin() {
        return this.encryptedPin;
    }

    public T setEncryptedPin(String pin) {
        this.encryptedPin = pin;
        return (T)this;
    }


    public String getUrl() {
        return this.url;
    }

    public T setUrl(String url) {
        this.url = url;
        return (T)this;
    }

    public String getAccountNumber10() {
        return this.accountNumber;
    }

    public String getAccountNumberStripped() {
        return this.accountNumber.replaceAll("^0+", "");
    }

    public T setAccountNumber(String accountNumber) {
        this.accountNumber = String.format("%10s", accountNumber).replace(' ', '0');
        return (T)this;
    }

    public String getBankCode() {
        return this.bankCode;
    }

    public T setBankCode(String bankCode) {
        this.bankCode = bankCode;
        return (T)this;
    }

    public String getHbciVersion() {
        return this.hbciVersion;
    }

    public T setHbciVersion(String hbciVersion) {
        this.hbciVersion = hbciVersion;
        return (T)this;
    }

    public String getCronScheduleExpression() {
        return this.cronScheduleExpression;
    }

    public T setCronScheduleExpression(String cronScheduleExpression) {
        this.cronScheduleExpression = cronScheduleExpression;
        return (T)this;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public T setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        return (T)this;
    }

    @Override
    public String toString() {
        return "AccountConnection{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", hbciVersion='" + hbciVersion + '\'' +
                ", cronScheduleExpression='" + cronScheduleExpression + '\'' +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }

    public T setCustomerId(String customerId) {
        this.customerId = customerId;
        return (T)this;
    }

    public String getCustomerId() {
        return customerId;
    }


}
