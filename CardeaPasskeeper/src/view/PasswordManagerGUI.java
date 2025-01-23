package view;


import controller.PasswordController;
import model.PasswordEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordManagerGUI {
    public static PasswordController controller = new PasswordController();

    private static final Color brown = new Color(101, 77, 56);
    private static final Color lightBrown = new Color(241, 189, 143);
    private static final Color lightGreen = new Color(201, 190, 127);
    private static final Color green = new Color(128, 162, 100);
    private static final Color darkGreen = new Color(87, 116, 77);

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cardea Passkeeper");
        frame.setLayout(new FlowLayout());

        createTextsFields(frame);

        createAddPasswordButton(frame);
        createRemovePasswordButton(frame);

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(lightBrown);
        frame.setResizable(false);
    }

    // Create Add Password Button and add logic to it
    public static void createAddPasswordButton(JFrame frame) {
        JButton addPasswordButton = new JButton("Add Password");
        frame.add(addPasswordButton);
        addPasswordButton.setBackground(green);

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
        removePasswordButton.setBackground(darkGreen);

        removePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PasswordEntry entry = new PasswordEntry("test", "username", "password");
                controller.removePassword(entry);
            }
        });
    }

    public static void createTextsFields(JFrame frame) {
        JTextField applicationName = new JTextField("");
        applicationName.setPreferredSize(new Dimension(200, 30));

        JTextField username = new JTextField("");
        username.setPreferredSize(new Dimension(200, 30));

        frame.add(applicationName);
        frame.add(username);
    }
}
