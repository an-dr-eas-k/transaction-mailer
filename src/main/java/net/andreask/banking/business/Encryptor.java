package net.andreask.banking.business;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.DatatypeConverter;

@RequestScoped
public class Encryptor {

    @Inject
    @Named("key")
    String key;

    @Inject
    @Named("initVecotr")
    String initVector;

    public String encrypt(String value) {
        return encrypt(value, key, initVector);
    }

    public static String encrypt(String value, String key, String initVector) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return DatatypeConverter.printBase64Binary(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public String decrypt(String value) {
        return decrypt(value, key, initVector);
    }

    public static String decrypt(String encrypted, String key, String initVector) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(DatatypeConverter.parseBase64Binary(encrypted));

            return new String(original);
        } catch (BadPaddingException ex) {
            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Produces
    @Named("key")
    public String requestForKey() {
        return "Bar12345Bar12345";
    }

    @Produces
    @Named("initVecotr")
    public String requestForInitVector() {
        return "RandomInitVector";
    }

}