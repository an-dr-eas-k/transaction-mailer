package net.andreask.banking.business;

import java.io.Serializable;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;

@RequestScoped
public class Encryptor implements Serializable {

  @Inject
  private Configuration configuration;

  public String encrypt(String value) {
    return encrypt(value, configuration.produceKey(), configuration.produceAlgorithmParameterSpec());
  }

  public static String encrypt(String decrypted, Key key, AlgorithmParameterSpec alParameterSpec) {
    if (decrypted == null) {
      return null;
    }
    try {

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(Cipher.ENCRYPT_MODE, key, alParameterSpec);

      byte[] encrypted = cipher.doFinal(decrypted.getBytes());

      return DatatypeConverter.printBase64Binary(encrypted);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  public String decrypt(String value) {
    return decrypt(value, configuration.produceKey(), configuration.produceAlgorithmParameterSpec());
  }

  public static String decrypt(String encrypted, Key key, AlgorithmParameterSpec alParameterSpec) {
    if (encrypted == null) {
      return null;
    }
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(Cipher.DECRYPT_MODE, key, alParameterSpec);

      byte[] original = cipher.doFinal(DatatypeConverter.parseBase64Binary(encrypted));

      return new String(original);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

}
