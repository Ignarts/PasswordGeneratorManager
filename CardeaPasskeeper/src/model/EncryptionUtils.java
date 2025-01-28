package model;

import controller.PasswordController;

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

            if (keyFile.exists() && keyFile.length() > 0) {
                // Cargar clave existente
                byte[] keyBytes = Base64.getDecoder().decode(new String(new FileInputStream(KEY_FILE).readAllBytes()));
                secretKey = new SecretKeySpec(keyBytes, "AES");
                System.out.println("Encryption key loaded successfully.");
            } else {
                // Generar nueva clave si no existe
                System.out.println("No existing key found. Generating a new key...");
                KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                keyGen.init(256);
                secretKey = keyGen.generateKey();
                saveSecretKey(secretKey);
                System.out.println("New encryption key generated and saved.");
            }

            // Desencriptar las contraseñas si es necesario (en el caso real)
            for (PasswordEntry entry : passwordList) {
                if (entry.getPassword().startsWith("{enc}")) {
                    entry.setPassword(decrypt(entry.getPassword()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during encryption initialization", e);
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

    public static String encrypt(String plainText) throws Exception {
        if (secretKey == null) {
            throw new IllegalStateException("Secret key is not initialized. Please check encryption setup.");
        }
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return "{enc}" + Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    public static String decrypt(String cipherText) throws Exception {
        if (secretKey == null) {
            throw new IllegalStateException("Secret key is not initialized. Please check the encryption setup.");
        }
        if (!cipherText.startsWith("{enc}")) {
            return cipherText; // Si no tiene el prefijo, ya está desencriptado
        }
        cipherText = cipherText.substring(5); // Eliminar el prefijo {enc}
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
    }

    public static SecretKey getSecretKey() {
        return secretKey;
    }
}
