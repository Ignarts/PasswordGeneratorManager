package model;

import controller.UserController;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;

public class EncryptionUtils {
    private static final String KEY_FILE = "data/secret.key";
    private static SecretKeySpec encryptionKey;

    public static void initializeEncryption() {
        encryptionKey = UserController.getEncryptionKey();
        if (encryptionKey == null) {
            throw new IllegalStateException("Encryption key is not initialized. Please log in again.");
        }
    }

    public static void saveSecretKey(SecretKey secretKey) {
        try (FileOutputStream fos = new FileOutputStream(KEY_FILE)) {
            fos.write(Base64.getEncoder().encode(secretKey.getEncoded()));
            System.out.println("Secret key saved successfully.");
        } catch (IOException e) {
            throw new RuntimeException("Error saving encryption key", e);
        }
    }

    public static String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
            return "{enc}" + Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    public static String decrypt(String cipherText) {
        try {
            if (!cipherText.startsWith("{enc}")) {
                return cipherText;
            }

            cipherText = cipherText.substring(5);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, encryptionKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }
}
