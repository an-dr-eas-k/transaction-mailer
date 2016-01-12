package net.andreask.banking.integration.hbci;

import net.andreask.banking.model.AccountTransaction;
import org.kapott.hbci.GV_Result.GVRKUms;

public class Mapper{
    private Mapper (){}

    public static AccountTransaction map(GVRKUms.UmsLine umsLine){
        return new AccountTransaction()
                .setAdditional(umsLine.additional)
                .setAddkey(umsLine.addkey)
                .setBdate(umsLine.bdate)
                .setCharge_value(umsLine.charge_value.getLongValue())
                .setCustomerref(umsLine.customerref)
                .setGvcode(umsLine.gvcode)
                .setInstref(umsLine.instref)
                .setIsSepa(umsLine.isSepa)
                .setIsStorno(umsLine.isStorno)
                .setPrimanota(umsLine.primanota)
                .setSaldo(umsLine.saldo.value.getLongValue())
                .setText(umsLine.text)
                .setUsage(umsLine.usage.stream().reduce(String::concat).get())
                .setValue(umsLine.value.getLongValue())
                .setValuta(umsLine.valuta);
    }
}
