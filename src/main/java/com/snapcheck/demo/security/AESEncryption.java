package com.snapcheck.demo.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
/* DO NOT use to encrypt data longer than 16 bytes as ECB is not 
 * cryptographically secure for large data sets
 */
public class AESEncryption {
	static Logger LOGGER = LoggerFactory.getLogger(AESEncryption.class);
	 
    private static SecretKeySpec secretKey=null;
    private static byte[] key=null;
 
    public AESEncryption() {
    	setKey(AESKeyManager.getKey());
    }
    
    private static void setKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        	LOGGER.error("Error setting encryption key", e);
        }
    }
 
    public String encrypt(String s) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(s.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | 
        		 IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e)
        {
        	LOGGER.error("Error encrypting", e);
            return null;
        }
    }
 
    public String decrypt(String s) {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(s)));
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
        		IllegalBlockSizeException | BadPaddingException e)
        {
        	LOGGER.error("Error decrypting", e);
        	return null;
        }
    }
}