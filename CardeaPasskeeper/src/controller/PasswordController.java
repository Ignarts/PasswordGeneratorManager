package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import org.json.JSONObject;

public class PasswordController {
    private static final String USER_FILE = "data/user.json";
    private static String storedUsername;
    private static String storedPasswordHash;

    public PasswordController() {
        loadUserCredentials();
    }

    private void loadUserCredentials() {
        File file = new File(USER_FILE);
        if (!file.exists()) {
            createNewUser();
        } else {
            try {
                String content = new String(Files.readAllBytes(Paths.get(USER_FILE)));
                JSONObject json = new JSONObject(content);
                storedUsername = json.getString("username");
                storedPasswordHash = json.getString("password");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error loading user credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createNewUser() {
        String username = JOptionPane.showInputDialog(null, "Choose a username:", "Setup", JOptionPane.QUESTION_MESSAGE);
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        String password = JOptionPane.showInputDialog(null, "Set your password:", "Setup", JOptionPane.QUESTION_MESSAGE);
        if (password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        try {
            String hashedPassword = Base64.getEncoder().encodeToString(password.getBytes());
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("password", hashedPassword);

            FileWriter fileWriter = new FileWriter(USER_FILE);
            fileWriter.write(json.toString());
            fileWriter.close();

            storedUsername = username;
            storedPasswordHash = hashedPassword;

            JOptionPane.showMessageDialog(null, "User created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saving user credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public boolean authenticateUser(String username, String password) {
        if (storedUsername == null || storedPasswordHash == null) {
            JOptionPane.showMessageDialog(null, "No user credentials found!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String hashedPassword = Base64.getEncoder().encodeToString(password.getBytes());
        return storedUsername.equals(username) && storedPasswordHash.equals(hashedPassword);
    }

    /**
     * Copia una contraseña al portapapeles del sistema.
     *
     * @param password La contraseña desencriptada a copiar.
     */
    public static void copyPasswordToClipboard(String password) {
        try {
            StringSelection stringSelection = new StringSelection(password);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);

            JOptionPane.showMessageDialog(
                    null,
                    "Password copied to clipboard!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error copying password to clipboard.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }
}
