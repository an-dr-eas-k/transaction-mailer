package net.andreask.transactionmailer.integration.hbci.hbci4java;

import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.structures.Konto;

import net.andreask.transactionmailer.domain.AccountTransaction;

public class Mapper {
    private Mapper() {
    }

    public static AccountTransaction map(GVRKUms.UmsLine umsLine) {

        return new AccountTransaction()
                .setAdditional(umsLine.additional)
                .setAddkey(umsLine.addkey)
                .setBdate(umsLine.bdate)
                .setCharge_value(umsLine.charge_value != null ? umsLine.charge_value.getLongValue() : -1)
                .setCustomerref(umsLine.customerref)
                .setGvcode(umsLine.gvcode)
                .setInstref(umsLine.instref)
                .setIsSepa(umsLine.isSepa)
                .setIsStorno(umsLine.isStorno)
                .setOther(map(umsLine.other))
                .setPrimanota(umsLine.primanota)
                .setSaldo(
                        umsLine.saldo != null && umsLine.saldo.value != null ? umsLine.saldo.value.getLongValue() : -1)
                .setText(umsLine.text)
                .setUsage(umsLine.usage != null ? umsLine.usage.stream().reduce(String::concat).get() : "")
                .setValue(umsLine.value.getLongValue())
                .setValuta(umsLine.valuta);
    }

    private static AccountTransaction.Konto map(Konto other) {
        return new AccountTransaction.Konto()
                .setAcctype(other.acctype)
                .setBic(other.bic)
                .setBlz(other.blz)
                .setCountry(other.country)
                .setCurr(other.curr)
                .setCustomerid(other.customerid)
                .setIban(other.iban)
                .setName(other.name)
                .setName2(other.name2)
                .setNumber(other.number)
                .setSubnumber(other.subnumber)
                .setType(other.type);
    }
}
