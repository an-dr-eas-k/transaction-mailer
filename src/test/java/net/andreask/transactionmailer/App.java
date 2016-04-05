package net.andreask.transactionmailer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import net.andreask.transactionmailer.integration.hbci.HbciAccessImpl;
import net.andreask.transactionmailer.integration.hbci.HbciFacade;
import net.andreask.transactionmailer.integration.hbci.hbci4java.HbciFacadeImpl;

/**
 * Hello world!
 */
public class App {

  private HbciFacade hbciFacade = new HbciFacadeImpl();

  public static void main(String[] args) throws IOException {
    System.out.println("Starting ...");
    new App().run();
    // Persistence.generateSchema("hv-pu", null);
  }

  public void run() throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("BLZ,CUSTOMERID[,KONTONR],PIN: ");

    String[] vals = br.readLine().split(",");

    HbciAccessImpl hai = new HbciAccessImpl()
        .setBankCode(vals[0])
        .setCustomerId(vals[1])
        .setAccountNumber(vals.length == 4 ? vals[2] : vals[1])
        .setPin(vals[vals.length - 1])
        .setHbciVersion("300");

    PrintWriter pw = new PrintWriter(new File("bin-test", String.format("%s.txt", hai.getGeneratedIban())));

    hbciFacade
        .setAccountConnection(hai)
        .init()
        .acquireTransactions()
        .stream()
        .forEach(pw::println);

  }
}
