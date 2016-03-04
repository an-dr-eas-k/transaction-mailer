package net.andreask.transactionmailer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
    System.out.print("encrypted PIN: ");

    hbciFacade
        .setAccountConnection(
            new HbciAccessImpl()
//            .setBankCode("70090500")
//            .setCustomerId("3964620")
//            .setAccountNumber("3964620")
//            .setEncryptedPin(br.readLine())
//            .setHbciVersion("300"))
                .setBankCode("70010080")
                .setCustomerId("4245801")
                .setAccountNumber("4245801")
                .setEncryptedPin(br.readLine())
                .setHbciVersion("300"))
        .init()
        .acquireTransactions()
        .stream()
        .forEach(System.out::println);

  }
}
