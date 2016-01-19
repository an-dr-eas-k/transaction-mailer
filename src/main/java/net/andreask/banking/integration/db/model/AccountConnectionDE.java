package net.andreask.banking.integration.db.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AccountConnectionDE {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String encryptedPin;
    private String url;
    private String accountNumber; //kontonummer
    private String bankCode; // blz
    private String hbciVersion;
    private String cronScheduleExpression; //format '<cron(minute)> <cron(hour)> <cron(dayOfWeek)>'
    private String customerId; // username beim bank login

    public int getId() {
        return this.id;
    }

    public AccountConnectionDE setId(int id) {
        this.id = id;
        return this;
    }



    public String getUrl() {
        return this.url;
    }

    public AccountConnectionDE setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public AccountConnectionDE setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public String getBankCode() {
        return this.bankCode;
    }

    public AccountConnectionDE setBankCode(String bankCode) {
        this.bankCode = bankCode;
        return this;
    }

    public String getHbciVersion() {
        return this.hbciVersion;
    }

    public AccountConnectionDE setHbciVersion(String hbciVersion) {
        this.hbciVersion = hbciVersion;
        return this;
    }

    public String getCronScheduleExpression() {
        return this.cronScheduleExpression;
    }

    public AccountConnectionDE setCronScheduleExpression(String scheduleExpression) {
        this.cronScheduleExpression = scheduleExpression;
        return this;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public AccountConnectionDE setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public String getEncryptedPin() {
        return this.encryptedPin;
    }

    public AccountConnectionDE setEncryptedPin(String encryptedPin) {
        this.encryptedPin = encryptedPin;
        return this;
    }
}
