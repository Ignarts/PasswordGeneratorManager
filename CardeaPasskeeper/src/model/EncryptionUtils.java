package model;

import controller.UserController;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;
import java.util.List;

public class EncryptionUtils {
    private static SecretKeySpec encryptionKey;

    public static void initializeEncryption() {
        encryptionKey = UserController.getEncryptionKey();
        if (encryptionKey == null) {
            throw new IllegalStateException("Encryption key is not initialized. Please log in again.");
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
}
