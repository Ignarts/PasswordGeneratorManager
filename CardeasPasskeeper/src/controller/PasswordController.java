package controller;

import model.PasswordEntry;

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
        passwordList.add(entry);
        savePassword();
    }

    public List<PasswordEntry> getPasswords(){
        return passwordList;
    }
}
