package controller;

import model.PasswordEntry;

import javax.swing.*;
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
        try(BufferedWriter writer = new BufferedWriter(new FileWriter((FILE_PATH)))){
            for(PasswordEntry entry : passwordList){
                writer.write(entry.getService() + "," + entry.getUsername() + "," + entry.getPassword());
                writer.newLine();
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public void loadPasswords(){
        passwordList = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))){
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if(parts.length == 3){
                    passwordList.add(new PasswordEntry(parts[0], parts[1], parts[2]));
                }
            }
        }catch (IOException e){
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
                    JOptionPane.PLAIN_MESSAGE
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
        passwordList.removeIf(passwordEntry -> passwordEntry.getService().equals(applicationName) &&
                passwordEntry.getUsername().equals(username));
        savePassword();
    }

    public List<PasswordEntry> getPasswords(){
        return passwordList;
    }
}
