package net.andreask.banking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.andreask.banking.integration.hbci.HbciAccessImpl;
import net.andreask.ll.integration.hbci.HbciFacade;
import net.andreask.ll.integration.hbci.hbci4java.HbciFacadeImpl;

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
    System.out.print("PIN: ");

    hbciFacade
        .setAccountConnection(
            new HbciAccessImpl()
                .setBankCode("70090500")
                .setCustomerId("3964620")
                .setAccountNumber("103964620")
                .setEncryptedPin(br.readLine())
                .setHbciVersion("300")
                .setUrl("fints.bankingonline.de/fints/FinTs30PinTanHttpGate")
                .setCountryCode("DE"))
        .init()
        .acquireTransactions()
        .stream()
        .forEach(System.out::println);

  }
}
