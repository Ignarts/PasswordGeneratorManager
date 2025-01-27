package controller;

import model.EncryptionUtils;
import model.PasswordEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PasswordController {
    private static final  String FILE_PATH = "data/passwords.txt";
    private List<PasswordEntry> passwordList;

    public PasswordController() {
        loadPasswords();  // Cargar contraseñas desde el archivo

        if (passwordList.isEmpty()) {
            System.out.println("No passwords found. Initializing encryption with empty list.");
        }

        EncryptionUtils.initializeEncryption(passwordList);  // Desencriptar y re-encriptar correctamente

        if (!passwordList.isEmpty()) {
            savePassword();  // Guardar con la nueva clave si hay contraseñas
        }
    }


    public void savePassword() {
        if (passwordList.isEmpty()) {
            System.out.println("No passwords to save. Skipping save operation.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (PasswordEntry entry : passwordList) {
                String encryptedPassword = EncryptionUtils.encrypt(entry.getPassword());
                writer.write(entry.getService() + "," + entry.getUsername() + "," + encryptedPassword);
                writer.newLine();
            }
            System.out.println("Passwords saved successfully.");
        } catch (Exception e) {
            System.err.println("Error saving passwords: " + e.getMessage());
        }
    }


    public void loadPasswords() {
        passwordList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    try {
                        passwordList.add(new PasswordEntry(parts[0], parts[1], parts[2]));
                    } catch (Exception e) {
                        System.err.println("Invalid password entry for service: " + parts[0]);
                    }
                }
            }
            System.out.println("Passwords loaded: " + passwordList.size());
        } catch (IOException e) {
            System.err.println("Error reading password file: " + e.getMessage());
        }
    }

    public void addPassword(PasswordEntry entry) {
        if (EncryptionUtils.getSecretKey() == null) {
            EncryptionUtils.initializeEncryption(passwordList);
        }

        if (EncryptionUtils.getSecretKey() == null) {
            JOptionPane.showMessageDialog(null,
                    "Encryption key is not available. Please restart the application.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean exists = passwordList.stream().anyMatch(passwordEntry ->
                passwordEntry.getService().equals(entry.getService()) &&
                        passwordEntry.getUsername().equals(entry.getUsername()));

        if (exists) {
            JOptionPane.showMessageDialog(null,
                    "Password already exists. Change username and app name.",
                    "Error Adding Password",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            entry.setPassword(EncryptionUtils.encrypt(entry.getPassword()));
            passwordList.add(entry);
            savePassword();
            JOptionPane.showMessageDialog(null,
                    "Password saved correctly",
                    "Password Added",
                    JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error saving password.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    public void removePassword(String applicationName, String username) {
        boolean removed = passwordList.removeIf(passwordEntry ->
                passwordEntry.getService().equals(applicationName) &&
                        passwordEntry.getUsername().equals(username)
        );

        if (removed) {
            JLabel messageLabel = new JLabel("Password removed successfully.");
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JOptionPane.showMessageDialog(
                    null,
                    messageLabel,
                    "Password Removed",
                    JOptionPane.PLAIN_MESSAGE
            );

            savePassword();
        } else {
            JLabel messageLabel = new JLabel("Password does not exist.");
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JOptionPane.showMessageDialog(
                    null,
                    messageLabel,
                    "Error Removing Password",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void copyPasswordToClipboard(String encryptedPassword) {
        try {
            String decryptedPassword = EncryptionUtils.decrypt(encryptedPassword);
            StringSelection stringSelection = new StringSelection(decryptedPassword);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);

            JOptionPane.showMessageDialog(
                    null,
                    "Password copied to clipboard successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error copying the password.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    public List<PasswordEntry> getPasswords(){
        return passwordList;
    }
}
