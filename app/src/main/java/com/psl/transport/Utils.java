package com.psl.transport;

import android.annotation.SuppressLint;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Utils {

    private static String publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDZxrO5S1zu2ScQjoUxownWXXqJeH+iUBqAmZGP0/oA1o0LOer4t4vfoJjFG5r3KPLDDvhBUfRwdmpXz8ooVRINKPgw7O/WPOzL6UIyjQou6XEec1H5vm4Ku9B0Gz9ImeNFuGePw5Z6CraudTh5lkABiSrhoobPUYN+xklEHayhGQIDAQAB";

    @SuppressLint("NewApi")
    public static String dothis (String text) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

            Base64.Encoder encoder = Base64.getEncoder();
            // 1. generate secret key using AES
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128); // AES is currently available in three key sizes: 128, 192 and 256 bits.The design and strength of all key lengths of the AES algorithm are sufficient to protect classified information up to the SECRET level
            SecretKey secretKey = keyGenerator.generateKey();

            //System.out.println("SecretKey: "+encoder.encodeToString(secretKey.getEncoded()));



            // 2. get string which needs to be encrypted
            //String text = "Jhoolay....";

            // 3. encrypt string using secret key
            byte[] raw = secretKey.getEncoded();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
            String envolpe = Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes(Charset.forName("UTF-8"))));
            //System.out.println("cipherTextString: "+cipherTextString);
           /* byte[] a= alpha(encoder.encodeToString(secretKey.getEncoded()));
            // 4. get public key
            X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicSpec);

            // 6. encrypt secret key using public key
            Cipher cipher2 = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
            cipher2.init(Cipher.ENCRYPT_MODE, publicKey);
            String delta = Base64.getEncoder().encodeToString(cipher2.doFinal(a));
//            System.out.println("encryptedSecretKey: "+encryptedSecretKey);

        String[] abc=new String[2];
        abc[0]=envolpe;abc[1]=delta;
        return abc;*/
           return  envolpe;
            // 7. pass cipherTextString (encypted sensitive data) and encryptedSecretKey to your server via your preferred way.
            // Tips:
            // You may use JSON to combine both the strings under 1 object.
            // You may use a volley call to send this data to your server.

    }

    public static byte[] alpha(String value) {

        String dd=value.substring(0, 3);
        String ee=value.substring(value.length()-3, value.length());
        String rem=value.substring(3,value.length()-3);
        rem=ee+rem+dd;

        return rem.getBytes();

    }
}
