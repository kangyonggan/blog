package com.kangyonggan.blog.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author kangyonggan
 * @since 5/4/18
 */
public final class Aes {

    private Aes() {}

    /**
     * 加密
     *
     * @param data
     * @param aesKey
     * @param aesIv
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String aesKey, String aesIv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        int blockSize = cipher.getBlockSize();
        byte[] dataBytes = data.getBytes();
        int plaintextLength = dataBytes.length;
        if (plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }
        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
        SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), "AES");
        IvParameterSpec parameterSpec = new IvParameterSpec(aesIv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec);
        byte[] encrypted = cipher.doFinal(plaintext);
        return Base64.encodeBase64String(encrypted);
    }

    /**
     * aes解密
     *
     * @param encrypted
     * @param aesKey
     * @param aesIv
     * @return
     * @throws Exception
     */
    public static String desEncrypt(String encrypted, String aesKey, String aesIv) throws Exception {
        byte[] encrypted1 = Base64.decodeBase64(encrypted);
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), "AES");
        IvParameterSpec parameterSpec = new IvParameterSpec(aesIv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, parameterSpec);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original);
        return originalString.trim();
    }

}
