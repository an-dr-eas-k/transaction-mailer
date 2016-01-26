package net.andreask.banking.model;

import java.io.Serializable;

import javax.ejb.ScheduleExpression;
import javax.enterprise.inject.Model;

import org.iban4j.CountryCode;
import org.iban4j.Iban;

@Model
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

	public void setId(int id) {
		this.id = id;
	}

	public String getEncryptedPin() {
		return this.encryptedPin;
	}

	public void setEncryptedPin(String pin) {
		this.encryptedPin = pin;

	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;

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

	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;

	}

	public String getHbciVersion() {
		return this.hbciVersion;
	}

	public void setHbciVersion(String hbciVersion) {
		this.hbciVersion = hbciVersion;

	}

	public String getCronScheduleExpression() {
		return this.cronScheduleExpression;
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

	@Override
	public String toString() {
		return "AccountConnection{" + "id=" + id + ", url='" + url + '\'' + ", accountNumber='" + accountNumber + '\''
				+ ", bankCode='" + bankCode + '\'' + ", hbciVersion='" + hbciVersion + '\''
				+ ", cronScheduleExpression='" + cronScheduleExpression + '\'' + ", countryCode='" + countryCode + '\''
				+ '}';
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;

	}

	public String getCustomerId() {
		return customerId;
	}

}
