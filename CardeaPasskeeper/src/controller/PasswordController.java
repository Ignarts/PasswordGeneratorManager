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

    public PasswordController(){
        loadPasswords();
    }

    public void savePassword(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (PasswordEntry entry : passwordList) {
                try {
                    String encryptedPassword = EncryptionUtils.encrypt(entry.getPassword());
                    writer.write(entry.getService() + "," + entry.getUsername() + "," + encryptedPassword);
                    writer.newLine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadPasswords(){
        passwordList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    try {
                        String decryptedPassword = EncryptionUtils.decrypt(parts[2].trim());
                        passwordList.add(new PasswordEntry(parts[0], parts[1], decryptedPassword));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e){
            passwordList = new ArrayList<>();
        }
    }

    public void addPassword(PasswordEntry entry){
        boolean exists = passwordList.stream().anyMatch(passwordEntry -> passwordEntry.getService().equals(entry.getService()) &&
                passwordEntry.getUsername().equals(entry.getUsername()));

        if(exists){
            JLabel messageLabel = new JLabel("Password already exists. Change username and app name.");
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JOptionPane.showMessageDialog(
                    null,
                    messageLabel,
                    "Error Adding Password",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        passwordList.add(entry);
        savePassword();

        JLabel messageLabel = new JLabel("Password saved correctly");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JOptionPane.showMessageDialog(
                null,
                messageLabel,
                "Password Added",
                JOptionPane.PLAIN_MESSAGE
        );
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

    public static void copyPasswordToClipboard(String encryptedPassword){
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
