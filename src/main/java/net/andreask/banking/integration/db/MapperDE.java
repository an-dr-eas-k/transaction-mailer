package net.andreask.banking.integration.db;

import net.andreask.banking.integration.db.model.AccountConnectionDE;
import net.andreask.banking.integration.db.model.AccountTransactionDE;
import net.andreask.banking.model.AccountConnection;
import net.andreask.banking.model.AccountTransaction;

public class Mapper {

	public static AccountTransaction map(AccountTransactionDE databaseEntry) {
		return new AccountTransaction()
				.setAdditional(databaseEntry.additional)
				.setAddkey(databaseEntry.addkey)
				.setBdate(databaseEntry.bdate)
				.setCharge_value(databaseEntry.charge_value)
				.setCustomerref(databaseEntry.customerref)
				.setGvcode(databaseEntry.gvcode)
				.setId(databaseEntry.getId())
				.setInstref(databaseEntry.instref)
				.setIsSepa(databaseEntry.getIsSepa())
				.setIsStorno(databaseEntry.getIsStorno())
				.setPrimanota(databaseEntry.primanota)
				.setSaldo(databaseEntry.saldo)
				.setText(databaseEntry.text)
				.setUsage(databaseEntry.usage)
				.setValue(databaseEntry.getValue())
				.setValuta(databaseEntry.valuta);
	}

	public static AccountTransactionDE map(AccountTransaction businessEntry) {
		return new AccountTransactionDE()
				.setAdditional(businessEntry.getAdditional())
				.setAddkey(businessEntry.getAddkey())
				.setBdate(businessEntry.getBdate())
				.setCharge_value(businessEntry.getCharge_value())
				.setCustomerref(businessEntry.getCustomerref())
				.setGvcode(businessEntry.getGvcode())
				.setId(businessEntry.getId())
				.setInstref(businessEntry.getInstref())
				.setIsSepa(businessEntry.getIsSepa())
				.setIsStorno(businessEntry.getIsStorno())
				.setPrimanota(businessEntry.getPrimanota())
				.setSaldo(businessEntry.getSaldo())
				.setText(businessEntry.getText())
				.setUsage(businessEntry.getUsage())
				.setValue(businessEntry.getValue())
				.setValuta(businessEntry.getValuta());
	}

	public static AccountConnectionDE map(AccountConnection ac) {
		return new AccountConnectionDE()
				.setId(ac.getId())
				.setAccountNumber(ac.getAccountNumber10())
				.setBankCode(ac.getBankCode())
				.setCustomerId(ac.getCustomerId())
				.setHbciVersion(ac.getHbciVersion())
				.setEncryptedPin(ac.getEncryptedPin())
				.setUrl(ac.getUrl())
				.setCronScheduleExpression(ac.getCronScheduleExpression());

	}

	public static AccountConnection map(AccountConnectionDE databaseEntry) {
		AccountConnection ac = new AccountConnection();
		ac.setId(databaseEntry.getId());
		ac.setAccountNumberStripped(databaseEntry.getAccountNumber());
		ac.setBankCode(databaseEntry.getBankCode());
		ac.setCustomerId(databaseEntry.getCustomerId());
		ac.setHbciVersion(databaseEntry.getHbciVersion());
		ac.setEncryptedPin(databaseEntry.getEncryptedPin());
		ac.setUrl(databaseEntry.getUrl());
		ac.setCronScheduleExpression(databaseEntry.getCronScheduleExpression());
		return ac;

	}

}
