package net.andreask.banking.integration.db;

import net.andreask.banking.integration.db.model.AccountConnectionDE;
import net.andreask.banking.integration.db.model.AccountTransactionDE;
import net.andreask.banking.model.AccountConnection;
import net.andreask.banking.model.AccountTransaction;

import javax.crypto.Cipher;
import java.util.Base64;

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
                .setAdditional(businessEntry.additional)
                .setAddkey(businessEntry.addkey)
                .setBdate(businessEntry.bdate)
                .setCharge_value(businessEntry.charge_value)
                .setCustomerref(businessEntry.customerref)
                .setGvcode(businessEntry.gvcode)
                .setId(businessEntry.getId())
                .setInstref(businessEntry.instref)
                .setIsSepa(businessEntry.getIsSepa())
                .setIsStorno(businessEntry.getIsStorno())
                .setPrimanota(businessEntry.primanota)
                .setSaldo(businessEntry.saldo)
                .setText(businessEntry.text)
                .setUsage(businessEntry.usage)
                .setValue(businessEntry.getValue())
                .setValuta(businessEntry.valuta);
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
        return new AccountConnection()
                .setId(databaseEntry.getId())
                .setAccountNumber(databaseEntry.getAccountNumber())
                .setBankCode(databaseEntry.getBankCode())
                .setCustomerId(databaseEntry.getCustomerId())
                .setHbciVersion(databaseEntry.getHbciVersion())
                .setEncryptedPin(databaseEntry.getEncryptedPin())
                .setUrl(databaseEntry.getUrl())
                .setCronScheduleExpression(databaseEntry.getCronScheduleExpression());

    }

//    private static String cipher(String input) {
//        com.sun.org.apache.xml.internal.security.utils.Base64.en
//
//        Cipher cipher = null;
//        try {
//            cipher = Cipher.getInstance("PBEWithMD5AndDES");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//            cipher.init(Cipher.DECRYPT_MODE, passportKey, paramspec);
//            cipher.
//                    o = null;
//            try {
//                o = new ObjectInputStream(new CipherInputStream(new FileInputStream(fname), cipher));
//
//            }
//        }
}
