package com.jieweifu.common.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class EncryptUtil {
    private static byte[] keys = new byte[]{83, 121, 115, 116, 101, 109, 46, 73};

    private static String encrypt(String valueToEnCrypt) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(keys);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(keys);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] encrypted = cipher.doFinal(valueToEnCrypt.getBytes("UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            for (byte anEncrypted : encrypted) {
                stringBuilder.append(String.format("%02x", anEncrypted));
            }
            return stringBuilder.toString().toUpperCase();
        } catch (Exception e) {
            return null;
        }
    }

    public static String decrypt(String valueToDeCrypt) {
        try {
            byte[] encrypted = new byte[valueToDeCrypt.length() / 2];
            for (int i = 0; i < valueToDeCrypt.length() / 2; i++) {
                String value = valueToDeCrypt.substring(i * 2, i * 2 + 2);
                encrypted[i] = (byte) (Integer.parseInt(value, 16) & 0xFF);
            }
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(keys);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(keys);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] original = cipher.doFinal(encrypted);
            return new String(original, "utf-8");
        } catch (Exception ex) {
            return null;
        }
    }

    public static void main(String[] args){
        System.out.println(encrypt("root"));
        System.out.println(encrypt("root3306"));
}
}
