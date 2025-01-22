package view;


import controller.PasswordController;
import model.PasswordEntry;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordManagerGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Password Manager");
        JButton addButton = new JButton("Add Password");
        frame.add(addButton);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPassword();
            }
        });
    }

    public static void addPassword() {
        PasswordController controller = new PasswordController();
        PasswordEntry entry = new PasswordEntry("test", "username", "password");
        //controller.removePassword(entry);
        //JOptionPane.showMessageDialog(null, "Password Added Successfully!");
    }
}
