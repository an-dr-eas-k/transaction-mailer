package net.andreask.transactionmailer.business;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Properties;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.faces.bean.RequestScoped;

@RequestScoped
public class Configuration {

  private Properties configProperties;

  public Configuration() {
    this.configProperties = provideProperties("hv.properties");
  }

  public Properties provideProperties(String resourceName) {
    try {
      Properties properties = new Properties();
      properties.load(this.getClass().getClassLoader().getResourceAsStream(resourceName));
      return properties;
    } catch (IOException e) {
      return null;
    }
  }

  public Key produceKey() {

    try {
      return new SecretKeySpec(getProperty("config.encryptor.key.secret_key_spec.aes").getBytes("UTF-8"), "AES");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public AlgorithmParameterSpec produceAlgorithmParameterSpec() {

    try {
      return new IvParameterSpec(getProperty("config.encryptor.algorithm_parameter_spec").getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public String getProperty(String key) {
    return this.configProperties.get(key).toString();
  }

}
