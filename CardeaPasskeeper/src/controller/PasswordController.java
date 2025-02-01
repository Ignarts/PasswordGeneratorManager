package controller;

import model.EncryptionUtils;
import model.PasswordEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.util.ArrayList;

public class PasswordController {
    private static final String PASSWORD_FILE = "data/passwords.txt";

    /**
     * Stores a password in the archive file, checking if it already exists.
     * @param service Service name (example: “Facebook”).
     * @param username Associated username.
     * @param plainPassword Password without encryption.
     */
    public static void savePassword(String service, String username, String plainPassword) {
        try {
            // Verificar si la contraseña ya existe
            if (passwordExists(service, username)) {
                JOptionPane.showMessageDialog(null, "Password for this service already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Encriptar la contraseña antes de guardarla
            String encryptedPassword = EncryptionUtils.encrypt(plainPassword);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(PASSWORD_FILE, true))) {
                writer.write(service + "," + username + "," + encryptedPassword);
                writer.newLine();
            }

            JOptionPane.showMessageDialog(null, "Password saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saving password.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Check if service or username exists, so there is a password for them.
     * @param service Service name.
     * @param username User name.
     * @return `true` if password exists, `false` if not.
     */
    public static boolean passwordExists(String service, String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PASSWORD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(service) && parts[1].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading password file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public static ArrayList<PasswordEntry> getPasswords(){
        ArrayList<PasswordEntry> passwordsList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(PASSWORD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                PasswordEntry passwordEntry = new PasswordEntry(parts[0], parts[1], parts[2]);
                passwordsList.add(passwordEntry);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading password file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return passwordsList;
    }

    public static boolean removePassword(String service, String username) {
        File file = new File(PASSWORD_FILE);
        File tempFile = new File("data/temp_passwords.txt");
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(service) && parts[1].equals(username)) {
                    found = true; // Se encontró la contraseña, no la escribimos en el nuevo archivo
                    continue;
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error modifying password file.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Si encontramos la contraseña, reemplazamos el archivo original
        if (found) {
            if (!file.delete() || !tempFile.renameTo(file)) {
                JOptionPane.showMessageDialog(null, "Error updating password file.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            JOptionPane.showMessageDialog(null, "Password removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            tempFile.delete(); // Si no se encontró, eliminamos el archivo temporal
            JOptionPane.showMessageDialog(null, "No matching password found.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static void copyPasswordToClipboard(String password) {
        try {
            StringSelection stringSelection = new StringSelection(password);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);

            JOptionPane.showMessageDialog(null, "Password copied to clipboard!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error copying password to clipboard.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
