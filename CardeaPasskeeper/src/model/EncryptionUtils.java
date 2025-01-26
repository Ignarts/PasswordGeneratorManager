package model;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;
import java.util.List;

public class EncryptionUtils {
    private static final String KEY_FILE = "data/secret.key";
    private static SecretKey secretKey;

    public static void initializeEncryption(List<PasswordEntry> passwordList) {
        try {
            File keyFile = new File(KEY_FILE);
            if (keyFile.exists()) {
                // Load current passwords
                byte[] keyBytes = Base64.getDecoder().decode(new String(new FileInputStream(KEY_FILE).readAllBytes()));
                secretKey = new SecretKeySpec(keyBytes, "AES");

                // Des-encrypted all password
                for (PasswordEntry entry : passwordList) {
                    entry.setPassword(decrypt(entry.getPassword()));
                }
            }

            // Generate new encryption key
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            secretKey = keyGen.generateKey();
            saveSecretKey(secretKey);

            // Re-Encrypt all passwords
            for (PasswordEntry entry : passwordList) {
                entry.setPassword(encrypt(entry.getPassword()));
            }

        } catch (Exception e) {
            throw new RuntimeException("Error during encryption initialization", e);
        }
    }

    private static void saveSecretKey(SecretKey secretKey) {
        try (FileOutputStream fos = new FileOutputStream(KEY_FILE)) {
            fos.write(Base64.getEncoder().encode(secretKey.getEncoded()));
        } catch (IOException e) {
            throw new RuntimeException("Error saving encryption key", e);
        }
    }

    public static String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    public static String decrypt(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
    }
}
