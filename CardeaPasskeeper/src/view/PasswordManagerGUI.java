package view;


import controller.PasswordController;
import model.PasswordEntry;
import model.PasswordManager;

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

    private static String name = "";
    private static String username = "";
    private static String password = "";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cardea Passkeeper");
        frame.setLayout(new BorderLayout());

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        createTextsFields(textPanel);
        frame.add(textPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        createAddPasswordButton(buttonPanel);
        createRemovePasswordButton(buttonPanel);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(lightBrown);
        frame.setResizable(false);
    }

    // Create Add Password Button and add logic to it
    public static void createAddPasswordButton(JPanel panel) {
        JButton addPasswordButton = new JButton("Add Password");
        panel.add(addPasswordButton);
        addPasswordButton.setBackground(green);

        addPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PasswordEntry entry = new PasswordEntry(name, username, PasswordManager.generatePassword(16, true, true, true));
                controller.addPassword(entry);
            }
        });
    }

    // Create Remove Button and add logic to it
    public static void createRemovePasswordButton(JPanel panel) {
        JButton removePasswordButton = new JButton("Remove Password");
        panel.add(removePasswordButton);
        removePasswordButton.setBackground(darkGreen);

        removePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PasswordEntry entry = new PasswordEntry("test", "username", "password");
                controller.removePassword(entry);
            }
        });
    }

    public static void createTextsFields(JPanel panel) {
        JTextField applicationName = new JTextField("");
        applicationName.setPreferredSize(new Dimension(200, 30));

        JTextField username = new JTextField("");
        username.setPreferredSize(new Dimension(200, 30));

        panel.add(applicationName);
        panel.add(username);
    }
}
