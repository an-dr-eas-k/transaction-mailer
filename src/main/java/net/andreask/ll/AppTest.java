package net.andreask.ll;

import java.io.IOException;

import javax.persistence.Persistence;

/**
 * Hello world!
 */
public class AppTest {


    public static void main(String[] args) throws IOException {
        System.out.println("Starting ...");

        Persistence.generateSchema("hv-pu", null);
    }

}
