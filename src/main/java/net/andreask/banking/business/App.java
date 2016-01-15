package net.andreask.banking.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.andreask.banking.integration.hbci.HbciSession;
import net.andreask.banking.model.AccountConnection;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("Starting ...");
        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        System.out.print("PIN: ");



        new HbciSession(
                new AccountConnection()
                        .setAccountNumber("3964620")
                        .setBankCode("70090500")
                        .setHbciVersion("300")
                        .setPin(Integer.parseInt(br.readLine()))
                        .setUrl("localhost")
                        .setCountryCode("DE"))
                .acquireTransactions()
                .stream()
                .forEach(System.out::println);

    }
}
