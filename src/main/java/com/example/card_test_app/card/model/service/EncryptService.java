package com.example.card_test_app.card.model.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptService {

    private final SecretKey secretKey;
    private final byte[] iv;

    private static final String SECRET_KEY_FILE = "secretKey.key";

    public EncryptService() throws Exception {
        this.secretKey = loadOrCreateSecretKey();

        this.iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
    }

    private SecretKey loadOrCreateSecretKey() throws Exception {
        File keyFile = new File(SECRET_KEY_FILE);
        if (keyFile.exists()) {
            try (FileInputStream fis = new FileInputStream(keyFile)) {
                byte[] keyBytes = new byte[16]; // 128-bit key
                fis.read(keyBytes);
                return new SecretKeySpec(keyBytes, "AES");
            }
        } else {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey key = keyGen.generateKey();
            saveSecretKey(key);
            return key;
        }
    }

    private void saveSecretKey(SecretKey key) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(SECRET_KEY_FILE)) {
            fos.write(key.getEncoded());
        }
    }

    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParams);

        byte[] encryptedData = cipher.doFinal(data.getBytes());

        byte[] encryptedWithIv = new byte[iv.length + encryptedData.length];
        System.arraycopy(iv, 0, encryptedWithIv, 0, iv.length);
        System.arraycopy(encryptedData, 0, encryptedWithIv, iv.length, encryptedData.length);

        return Base64.getEncoder().encodeToString(encryptedWithIv);
    }

    public String decrypt(String encryptedData) throws Exception {
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);

        byte[] iv = new byte[16];
        System.arraycopy(decodedData, 0, iv, 0, iv.length);

        byte[] cipherText = new byte[decodedData.length - iv.length];
        System.arraycopy(decodedData, iv.length, cipherText, 0, cipherText.length);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams);

        byte[] decryptedData = cipher.doFinal(cipherText);

        return new String(decryptedData);
    }
}
