package net.andreask.banking;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import net.andreask.banking.business.Configuration;
import net.andreask.banking.business.XMapper;
import net.andreask.banking.domain.AccountTransaction;
import net.andreask.banking.integration.hbci.HbciAccessImpl;
import net.andreask.banking.integration.hbci.hbci4java.HbciFacadeImpl;

/**
 * Hello world!
 */
public class PrepareHBCIMock {

  public static void main(String[] args) throws IOException {
    Configuration c = new Configuration();
    System.out.println("Starting ...");
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("PIN: ");

    List<AccountTransaction> result = new HbciFacadeImpl()
        .setAccountConnection(
            new HbciAccessImpl()
                .setBankCode("70090500")
                .setCustomerId("3964620")
                .setAccountNumber("103964620")
                .setPin(br.readLine())
                .setHbciVersion("300")
                .setUrl("fints.bankingonline.de/fints/FinTs30PinTanHttpGate")
                .setCountryCode("DE"))
        .init()
        .acquireTransactions();

    FileWriter fw = new FileWriter(c.getProperty("hbcimock.data.file"));
    fw.write(XMapper.toXml(result));
    fw.close();
  }

}
