package net.andreask.ll.business;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import net.andreask.ll.business.Configuration;
import net.andreask.ll.business.Encryptor;

/**
 * Created by andreask on 1/19/16.
 */
public class EncryptorTest {

  private Configuration c;

  @BeforeClass
  public void init() {
    c = new Configuration();
  }

  @Test
  public void testEncrypt() throws Exception {
    Assert.assertEquals(Encryptor.encrypt("Hello World", c.produceKey(), c.produceAlgorithmParameterSpec()),
        "9MU7vSBqfzPnj7iWvvfsEw==");
  }

  @Test
  public void testDecrypt() throws Exception {
    Assert.assertEquals(
        Encryptor.decrypt("9MU7vSBqfzPnj7iWvvfsEw==", c.produceKey(), c.produceAlgorithmParameterSpec()),
        "Hello World");
  }

  @Test
  public void both() throws Exception {
    String start = "Hello World";
    Assert.assertEquals(start,
        Encryptor.decrypt(
            Encryptor.encrypt(start, c.produceKey(), c.produceAlgorithmParameterSpec()),
            c.produceKey(),
            c.produceAlgorithmParameterSpec()));
  }

  @Test
  public void both2() throws Exception {
    String start = "Hello World";
    Assert.assertNotEquals(start,
        Encryptor.decrypt(
            Encryptor.encrypt(start, c.produceKey(),
                new IvParameterSpec(c.getProperty("").replace('i', 'j').getBytes())),
            c.produceKey(), c.produceAlgorithmParameterSpec()));
  }

  @Test
  public void both3() throws Exception {
    String start = "Hello World";
    Assert.assertNotEquals(start,
        Encryptor.decrypt(
            Encryptor.encrypt(
                start,
                new SecretKeySpec(
                    c.getProperty("config.encryptor.key.secret_key_spec.aes").replace('1', '2').getBytes(), "AES"),
                c.produceAlgorithmParameterSpec()),
            c.produceKey(),
            c.produceAlgorithmParameterSpec()));
  }

  public static void main(String[] args) throws Exception {
    Configuration c = new Configuration();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("Enter source String: ");
    String toEncrypt = br.readLine();
    System.out.printf("encryption.\nsource: '%s'\ntarget: '%s'",
        toEncrypt,
        Encryptor.encrypt(toEncrypt, c.produceKey(), c.produceAlgorithmParameterSpec()));
  }
}
