/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.andreask.transactionmailer.integration.hbci.hbci4java;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.GV_Result.GVRSaldoReq;
import org.kapott.hbci.GV_Result.GVRSaldoReq.Info;
import org.kapott.hbci.callback.HBCICallbackConsole;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.passport.HBCIPassportPinTan;
import org.kapott.hbci.structures.Konto;

import net.andreask.transactionmailer.domain.AccountTransaction;
import net.andreask.transactionmailer.integration.hbci.HbciAccess;

/**
 * @author alex
 */
public class HbciSession {

  private final HbciAccess accountConnection;
  private static Logger logger = LogManager.getLogger(HbciSession.class);
  private HBCIHandler handler;

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
        retData.append(HbciSession.this.accountConnection.getCustomerId());
        break;

      case NEED_USERID:
        retData.append(HbciSession.this.accountConnection.getCustomerId());
        break;

      case NEED_PT_PIN:
        retData.append(HbciSession.this.accountConnection.getPin());
        break;

      case NEED_PASSPHRASE_SAVE:
      case NEED_PASSPHRASE_LOAD:
        retData.append(PASSPHRASE);
        break;

      case NEED_PT_SECMECH:

        retData.setLength(0);
        retData.append("997");
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

  public HbciSession(HbciAccess a) {
    this.accountConnection = a;
    this.initParams();
  }

  private HBCIHandler createHbciHandler() {
    HBCIPassportPinTan passport = new HBCIPassportNonPersistentPinTan("");

    return new HBCIHandler(this.accountConnection.getHbciVersion(), passport);
  }

  private void initParams() {
    HBCIUtils.init(null, new Callback());

    // Set basic parameters
    HBCIUtils.setParam("client.passport.hbciversion.default", accountConnection.getHbciVersion());
    HBCIUtils.setParam("client.connection.localPort", null);
    HBCIUtils.setParam("log.loglevel.default", "5");
    HBCIUtils.setParam("kernel.rewriter", HBCIUtils.getParam("kernel.rewriter"));

    // Configure for PinTan
    HBCIUtils.setParam("client.passport.PinTan.checkcert", "1");
    HBCIUtils.setParam("client.passport.PinTan.certfile", null);
    HBCIUtils.setParam("client.passport.PinTan.init", "0");

    // Set path & passport implementation for passport
    HBCIUtils.setParam("client.passport.default", "NonPersistentPinTan");
    HBCIUtils.setParam("client.passport.PinTan.filename", "template");
  }

  public void initSession() {
    this.handler = createHbciHandler();
  }

  public Konto findAccount() {
    Konto[] accounts = handler.getPassport().getAccounts();

    for (Konto account : accounts) {
      if (this.accountConnection.getBankCode().equals(account.blz)
          && this.accountConnection.getAccountNumberStripped().equals(account.number))
        return account;
    }

    if (accounts.length == 1){
    	logger.info("account {} not found, taking the one which was found");
    	return accounts[0];
    }
    
    throw new IllegalStateException(String.format(
        "Unable to find requested account %s, %s",
        this.accountConnection.getAccountNumberStripped(),
        this.accountConnection.getBankCode()));
  }

  public long acquireBalance() {

    Konto k = this.findAccount();
    logger.info("Have account: " + k);

    HBCIJob job = handler.newJob("SaldoReq");
    job.setParam("my", k);

    job.addToQueue();

    handler.execute();

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

    Konto k = this.findAccount();
    logger.info("Using account " + k);

    HBCIJob job = handler.newJob("KUmsAll");
    job.setParam("my", k);

    job.addToQueue();

    handler.execute();

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
