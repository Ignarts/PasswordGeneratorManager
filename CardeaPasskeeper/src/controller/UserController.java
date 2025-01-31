package controller;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.io.*;
import java.security.SecureRandom;
import java.util.Base64;
import org.json.JSONObject;

public class UserController {
    private static final String USER_FILE = "data/user.dat"; // Archivo encriptado
    private static final String SALT_FILE = "data/salt.dat";
    private static final int KEY_LENGTH = 256;
    private static final int ITERATIONS = 65536;

    private static byte[] salt;
    private static SecretKeySpec encryptionKey;

    public static boolean isUserRegistered() {
        return new File(USER_FILE).exists();
    }

    public static void createUser() {
        String username = JOptionPane.showInputDialog("Enter a username:");
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String password = JOptionPane.showInputDialog("Enter a password:");
        if (password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        generateSalt();
        deriveKeyFromPassword(password);

        JSONObject userJson = new JSONObject();
        userJson.put("username", username);
        userJson.put("password", password); // Se almacenará encriptado

        try (FileOutputStream fos = new FileOutputStream(USER_FILE)) {
            fos.write(encrypt(userJson.toString().getBytes()));
            JOptionPane.showMessageDialog(null, "User registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving user data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static boolean validateUser(String password) {
        try {
            deriveKeyFromPassword(password);

            byte[] encryptedData = new FileInputStream(USER_FILE).readAllBytes();
            String decryptedData = new String(decrypt(encryptedData));

            JSONObject userJson = new JSONObject(decryptedData);
            String storedPassword = userJson.getString("password");

            return storedPassword.equals(password);

        } catch (Exception e) {
            return false;
        }
    }

    private static void generateSalt() {
        salt = new byte[32];
        new SecureRandom().nextBytes(salt);

        try (FileOutputStream fos = new FileOutputStream(SALT_FILE)) {
            fos.write(salt);
        } catch (IOException e) {
            throw new RuntimeException("Error saving salt", e);
        }
    }

    private static void loadSalt() {
        try (FileInputStream fis = new FileInputStream(SALT_FILE)) {
            salt = fis.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Error loading salt", e);
        }
    }

    private static void deriveKeyFromPassword(String password) {
        loadSalt();
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] keyBytes = factory.generateSecret(spec).getEncoded();
            encryptionKey = new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            throw new RuntimeException("Error deriving encryption key", e);
        }
    }

    private static byte[] encrypt(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    private static byte[] decrypt(byte[] encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, encryptionKey);
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }

    public static SecretKeySpec getEncryptionKey() {
        if (encryptionKey == null) {
            throw new IllegalStateException("Encryption key has not been initialized. User must log in first.");
        }
        return encryptionKey;
    }
}