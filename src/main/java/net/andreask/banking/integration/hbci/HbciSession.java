/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.andreask.banking.integration.hbci;

import java.util.List;
import java.util.stream.Collectors;

import net.andreask.banking.integration.db.model.AccountConnectionDE;
import net.andreask.banking.model.AccountTransaction;
import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.GV_Result.GVRSaldoReq;
import org.kapott.hbci.GV_Result.GVRSaldoReq.Info;
import org.kapott.hbci.callback.HBCICallbackConsole;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.passport.AbstractHBCIPassport;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.status.HBCIExecStatus;
import org.kapott.hbci.structures.Konto;

/**
 * @author alex
 */
public class HbciSession {

    private final AccountConnectionDE accountConnection;

    private class Callback extends HBCICallbackConsole {

        private static final String PASSPHRASE = "238dsflqJSSD:__sda3";

        @Override
        public synchronized void status(HBCIPassport passport, int statusTag,
                Object[] o) {
            // Intentionally empty
        }

        @Override
        public void callback(HBCIPassport passport, int reason, String msg,
                int datatype, StringBuffer retData) {
            HBCIUtils.log("[LOG] " + msg + " / Reason: " + reason
                    + " / datatype: " + datatype, HBCIUtils.LOG_DEBUG);

            switch (reason) {
            case NEED_BLZ:
                retData.append(HbciSession.this.accountConnection.getBankCode());
                break;

            case NEED_CUSTOMERID:
                if (null != HbciSession.this.accountConnection.getAccountNumber()) {
                    retData.append(HbciSession.this.accountConnection.getAccountNumber());
                }
                break;

            case NEED_USERID:
                if (null != HbciSession.this.accountConnection.getAccountNumber()) {
                    retData.append(HbciSession.this.accountConnection.getAccountNumber());
                }
                break;

            case NEED_PT_PIN:
                retData.append(HbciSession.this.accountConnection.getPin());
                break;

            case NEED_PASSPHRASE_SAVE:
            case NEED_PASSPHRASE_LOAD:
                retData.append(PASSPHRASE);
                break;

            case NEED_PT_SECMECH:
                retData.append("mobileTAN");
                break;

            case NEED_COUNTRY:
            case NEED_HOST:
            case NEED_CONNECTION:
            case CLOSE_CONNECTION:
            default:
                // Intentionally empty!
            }

            HBCIUtils.log("Returning " + retData.toString(),
                    HBCIUtils.LOG_DEBUG);
        }
    }

    public HbciSession(AccountConnectionDE a) {
        this.accountConnection = a;
        this.initialize();
    }

    public void logIn() {
        HBCIHandler handle = this.createHbciHandler();
    }

    private HBCIHandler createHbciHandler() {
        HBCIPassport passport = AbstractHBCIPassport.getInstance();

        HBCIHandler handle = new HBCIHandler(this.accountConnection.getHbciVersion(), passport);
        return handle;
    }

    public void clearCachedDetails(HBCIPassport passport) {
        passport.clearBPD();
        passport.clearUPD();
    }

    private void initialize() {
        HBCIUtils.init(null, new Callback());

        // Set basic parameters
        HBCIUtils.setParam("client.passport.hbciversion.default", accountConnection.getHbciVersion());
        HBCIUtils.setParam("client.connection.localPort", null);
        HBCIUtils.setParam("log.loglevel.default", "3");
        HBCIUtils.setParam("kernel.rewriter",
                HBCIUtils.getParam("kernel.rewriter"));

        // Configure for PinTan
        HBCIUtils.setParam("client.passport.PinTan.checkcert", "1");
        HBCIUtils.setParam("client.passport.PinTan.certfile", null);
        HBCIUtils.setParam("client.passport.PinTan.init", "1");

        // Set path & passport implementation for passport
        HBCIUtils
                .setParam("client.passport.default", "NonPersistentPinTan");
        HBCIUtils.setParam("client.passport.PinTan.filename", null);
    }

    public Konto findAccount(HBCIHandler handle) {
        Konto[] accounts = handle.getPassport().getAccounts();

        for (Konto account : accounts) {
            if (this.accountConnection.getBankCode().equals(account.blz)
                    && this.accountConnection.getAccountNumber().equals(account.number))
                return account;
        }

        throw new IllegalStateException(String.format(
                "Unable to find requested account %s, %s",
                this.accountConnection.getAccountNumber(),
                this.accountConnection.getBankCode()));
    }

    public long acquireBalance() {
        HBCIHandler handle = this.createHbciHandler();

        Konto k = this.findAccount(handle);
        System.out.println("Have account: " + k);

        HBCIJob job = handle.newJob("SaldoReq");
        job.setParam("my", k);

        job.addToQueue();

        HBCIExecStatus ret = handle.execute();

        GVRSaldoReq result = (GVRSaldoReq) job.getJobResult();

        if (!result.isOK()) {
            throw new IllegalStateException("Fetching balance failed: "
                    + result.getJobStatus().getErrorString() + " / "
                    + result.getGlobStatus().getErrorString());
        }

        for (Info info : result.getEntries()) {
            if (!k.equals(info.konto))
                continue;
            return info.ready.value.getLongValue();

        }
        return -1;
    }

    public List<AccountTransaction> acquireTransactions() {
        HBCIHandler handle = this.createHbciHandler();

        Konto k = this.findAccount(handle);
        System.out.println("Using account " + k);

        HBCIJob job = handle.newJob("KUmsAll");
        job.setParam("my", k);

        job.addToQueue();

        HBCIExecStatus ret = handle.execute();

        GVRKUms jobResult = (GVRKUms) job.getJobResult();
        if (!jobResult.isOK()) {
            throw new IllegalStateException("Fetching balance failed: "
                    + jobResult.getJobStatus().getErrorString() + " / "
                    + jobResult.getGlobStatus().getErrorString());
        }

        return jobResult
                .getFlatData()
                .stream()
                .map(Mapper::map)
                .collect(Collectors.toList());

    }

}
