package net.andreask.banking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.inject.Inject;
import javax.ws.rs.Produces;

import net.andreask.banking.integration.hbci.HbciFacade;
import net.andreask.banking.model.AccountConnection;

/**
 * Hello world!
 */
public class App {

    @Inject
    private HbciFacade hbciFacade = new HbciFacadeTestAlternative();

    @Produces
    public static Properties provideProperties() {
        try {
            Properties properties = new Properties();
            properties.load(PrepareHBCIMock.class.getClassLoader().getResourceAsStream("hv.properties"));
            return properties;
        } catch (IOException e) {
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Starting ...");
        new App().run();
//        Persistence.generateSchema("hv-pu", null);
    }

    public void run() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("PIN: ");

        hbciFacade
                .setAccountConnection(
                        new AccountConnection()
                                .setBankCode("70090500")
                                .setCustomerId("3964620")
                                .setAccountNumber("103964620")
                                .setPin(Integer.parseInt(br.readLine()))
                                .setHbciVersion("300")
                                .setUrl("fints.bankingonline.de/fints/FinTs30PinTanHttpGate")
                                .setCountryCode("DE"))
                .init()
                .acquireTransactions()
                .stream()
                .forEach(System.out::println);

    }
}
