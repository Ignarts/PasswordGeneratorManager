package view;


import controller.PasswordController;
import model.PasswordEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordManagerGUI {
    public static PasswordController controller = new PasswordController();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Password Manager");
        frame.setLayout(new FlowLayout());

        createAddPasswordButton(frame);
        createRemovePasswordButton(frame);

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Create Add Password Button and add logic to it
    public static void createAddPasswordButton(JFrame frame) {
        JButton addPasswordButton = new JButton("Add Password");
        frame.add(addPasswordButton);

        addPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PasswordEntry entry = new PasswordEntry("test", "username", "password");
                controller.addPassword(entry);
            }
        });
    }

    // Create Remove Button and add logic to it
    public static void createRemovePasswordButton(JFrame frame) {
        JButton removePasswordButton = new JButton("Remove Password");
        frame.add(removePasswordButton);

        removePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PasswordEntry entry = new PasswordEntry("test", "username", "password");
                controller.removePassword(entry);
            }
        });
    }
}
