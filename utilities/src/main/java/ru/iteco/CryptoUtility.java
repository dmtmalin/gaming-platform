package ru.iteco;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;

@Component
public class CryptoUtility {

    private final String ALGORITHM = "AES/CBC/PKCS5PADDING";

    public String encrypt(String key, String text) throws GeneralSecurityException, UnsupportedEncodingException {
        String charSet = "UTF-8";
        byte[] byteKey = getAESKey(key, charSet);
        byte[] vector = getVector(charSet);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(vector);
        SecretKeySpec secretKeySpec = new SecretKeySpec(byteKey, "AES");

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encrypted = cipher.doFinal(text.getBytes());
        String ss = Base64.encodeBase64URLSafeString(encrypted);
        String s = decrypt(key, ss);
        return ss;
    }

    public String decrypt(String key, String encryptedText) throws GeneralSecurityException, UnsupportedEncodingException {
        String charSet = "UTF-8";
        byte[] byteKey = getAESKey(key, charSet);
        byte[] vector = getVector(charSet);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(vector);
        SecretKeySpec secretKeySpec = new SecretKeySpec(byteKey, "AES");

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = Base64.decodeBase64(encryptedText);
        byte[] original = cipher.doFinal(encrypted);
        return new String(original);
    }

    private byte[] getAESKey(String key, String charSet) throws GeneralSecurityException, UnsupportedEncodingException {
        byte[] byteKey = key.getBytes(charSet);
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        byteKey = sha.digest(byteKey);
        return Arrays.copyOf(byteKey, 16);
    }

    private byte[] getVector(String charSet) throws GeneralSecurityException, UnsupportedEncodingException {
        return getAESKey("BeccnZqYypkiqFitqMemSa3o5HNGSKdt", charSet);
    }
}
