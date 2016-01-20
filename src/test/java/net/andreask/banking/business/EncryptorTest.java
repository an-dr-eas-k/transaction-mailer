package net.andreask.banking.business;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by andreask on 1/19/16.
 */
public class EncryptorTest {

    String key = "Bar12345Bar12345"; // 128 bit key
    String initVector = "RandomInitVector"; // 16 bytes IV

    @Deployment
    public static Archive<?> createDeployment() {
        WebArchive ja = ShrinkWrap.create(WebArchive.class)
                .addPackage(Encryptor.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(ja.toString(true));
        return ja;
    }

    @Inject
    Encryptor e;

    @Test
    public void testEncrypt() throws Exception {
        Assert.assertEquals(Encryptor.encrypt(key, initVector, "Hello World"), "9MU7vSBqfzPnj7iWvvfsEw==");
    }

    @Test
    public void testEncryptServer() throws Exception {

        Assert.assertEquals(e.encrypt("Hello World"), "9MU7vSBqfzPnj7iWvvfsEw==");
    }

    @Test
    public void testDecrypt() throws Exception {
        Assert.assertEquals(Encryptor.decrypt(key, initVector, "9MU7vSBqfzPnj7iWvvfsEw=="), "Hello World");
    }

    @Test
    public void testDecryptServer() throws Exception {
        Assert.assertEquals(e.decrypt("9MU7vSBqfzPnj7iWvvfsEw=="), "Hello World");
    }

    @Test
    public void both() throws Exception {
        String start = "Hello World";
        Assert.assertEquals(start, Encryptor.decrypt(key, initVector, Encryptor.encrypt(key, initVector, start)));
    }

    @Test
    public void both2() throws Exception {
        String start = "Hello World";
        Assert.assertNotEquals(start,
                Encryptor.decrypt(key, initVector, Encryptor.encrypt(key, initVector.replace('i', 'j'), start)));
    }

    @Test
    public void both3() throws Exception {
        String start = "Hello World";
        Assert.assertNotEquals(start,
                Encryptor.decrypt(key, initVector, Encryptor.encrypt(key.replace('a', 'b'), initVector, start)));
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter source String: ");
        String toEncrypt = br.readLine();
        System.out.printf("encryption.\nsource: '%s'\ntarget: '%s'",
                toEncrypt,
                Encryptor.encrypt(toEncrypt, "Bar12345Bar12345", "RandomInitVector"));
    }
}
