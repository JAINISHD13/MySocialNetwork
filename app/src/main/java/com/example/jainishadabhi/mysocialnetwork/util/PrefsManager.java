package com.example.jainishadabhi.mysocialnetwork.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.jainishadabhi.mysocialnetwork.R;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class PrefsManager {

    private Context context;
    private SharedPreferences sharedPreferences;
    byte[] ciphertext;

    public PrefsManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public void clearData() {
        sharedPreferences.edit().clear().commit();
    }

    public void saveData(String key, int value) {
        sharedPreferences.edit().putInt(key, value).commit();

    }


    public void saveData(String key, String value) {

        sharedPreferences.edit().putString(key, value).commit();

    }


    public void saveData(String key, long value) {
        sharedPreferences.edit().putLong(key, value).commit();

    }

    public void saveData(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();

    }

    public String getStringData(String key){

        return sharedPreferences.getString(key, "");
    }


    public int getIntData(String key)
    {
        return sharedPreferences.getInt(key, -1);
    }


    public long getLongData(String key)
    {
        return sharedPreferences.getLong(key, -1);
    }

    public boolean getBoolData(String key)
    {
        return sharedPreferences.getBoolean(key, false);
    }


    public static byte[] encrypt(byte[] plaintext, SecretKey key) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] cipherText = cipher.doFinal(plaintext);
        return cipherText;
    }

    public static byte[] decrypt(byte[] cipherText, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(cipherText);
            return decrypted;
    }

    public static byte[] encryptAsymmetric(byte[] plaintext, PublicKey key) throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = cipher.doFinal(plaintext);
        return cipherText;
    }

    public static byte[] decryptAsymmetric(byte[] cipherText, PrivateKey key)
    {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE,key);
            byte[] decryptedText = cipher.doFinal(cipherText);
            return decryptedText;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    public String encrypt(byte[] strToEncrypt, String secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
          // byte[] bytes = Base64.decode(strToEncrypt,Base64.DEFAULT);
                    //strToEncrypt.getBytes();
            byte[] decodedKey = Base64.decode(secretKey, Base64.DEFAULT);
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, originalKey);
            ciphertext = cipher.doFinal(strToEncrypt);
            return new String(Base64.encode(ciphertext, Base64.DEFAULT));
      //  return null;
    }

    public String decrypt(String strToDecrypt, String secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
       *//* byte[] b = Base64.decode(strToDecrypt,Base64.DEFAULT);
                //strToDecrypt.getBytes();
        System.out.println("IsEqual :" + Arrays.equals(ciphertext,b));
        byte[] decodedKey = Base64.decode(secretKey,Base64.DEFAULT);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,originalKey);
        byte[] deciphertext = cipher.doFinal(b);
        return new String(Base64.encode(deciphertext, Base64.DEFAULT));*//*
        return null;
    }*/
}
