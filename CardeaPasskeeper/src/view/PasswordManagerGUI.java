package view;

import controller.PasswordController;
import model.EncryptionUtils;
import model.PasswordManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class PasswordManagerGUI {

    private static final Color brown = new Color(101, 77, 56);
    private static final Color lightBrown = new Color(241, 189, 143);
    private static final Color lightGreen = new Color(201, 190, 127);
    private static final Color green = new Color(128, 162, 100);
    private static final Color darkGreen = new Color(87, 116, 77);

    private static JTextField applicationNameTextField;
    private static JTextField usernameTextField;
    private static JCheckBox upperCheckBox;
    private static JCheckBox digitCheckBox;
    private static JCheckBox specialCharCheckBox;

    private static final String PASSWORD_FILE = "data/passwords.txt";

    public static void runApplication() {
        JFrame frame = new JFrame("Cardea Passkeeper");
        frame.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Panel para los textos
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        createTextsFields(textPanel);
        centerPanel.add(textPanel);

        // Botón de copiar
        JPanel copyButtonPanel = new JPanel();
        copyButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        createCopyPasswordButton(copyButtonPanel);
        centerPanel.add(copyButtonPanel);

        frame.add(centerPanel, BorderLayout.CENTER);

        // Panel inferior con checkboxes y botones
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(2, 1, 5, 5));

        // Checkboxes
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        createCheckboxesFields(checkBoxPanel);
        southPanel.add(checkBoxPanel);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        createAddPasswordButton(buttonPanel);
        createRemovePasswordButton(buttonPanel);
        southPanel.add(buttonPanel);

        frame.add(southPanel, BorderLayout.SOUTH);

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(lightBrown);
        frame.setResizable(false);
    }

    public static void createAddPasswordButton(JPanel panel) {
        JButton addPasswordButton = new JButton("Add Password");
        panel.add(addPasswordButton);
        addPasswordButton.setBackground(green);

        addPasswordButton.addActionListener(e -> {
            String name = applicationNameTextField.getText().trim();
            String username = usernameTextField.getText().trim();
            String password = PasswordManager.generatePassword(16, upperCheckBox.isSelected(), digitCheckBox.isSelected(), specialCharCheckBox.isSelected());

            if (name.isEmpty() || username.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter both Service and Username.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PasswordController.savePassword(name, username, password);
        });
    }

    public static void createRemovePasswordButton(JPanel panel) {
        JButton removePasswordButton = new JButton("Remove Password");
        panel.add(removePasswordButton);
        removePasswordButton.setBackground(darkGreen);

        removePasswordButton.addActionListener(e -> {
            String name = applicationNameTextField.getText().trim();
            String username = usernameTextField.getText().trim();

            if (name.isEmpty() || username.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter both Service and Username.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PasswordController.removePassword(name, username);
        });
    }

    public static void createTextsFields(JPanel panel) {
        applicationNameTextField = new JTextField("", 15);
        usernameTextField = new JTextField("", 15);

        panel.add(applicationNameTextField);
        panel.add(usernameTextField);
    }

    public static void createCheckboxesFields(JPanel panel) {
        upperCheckBox = new JCheckBox("Upper Letters");
        digitCheckBox = new JCheckBox("Number Digits");
        specialCharCheckBox = new JCheckBox("Special Characters");

        panel.add(upperCheckBox);
        panel.add(digitCheckBox);
        panel.add(specialCharCheckBox);
    }

    public static void createCopyPasswordButton(JPanel panel) {
        JButton copyPasswordButton = new JButton("Copy Password");
        copyPasswordButton.setBackground(lightGreen);

        copyPasswordButton.addActionListener(e -> {
            String name = applicationNameTextField.getText().trim();
            String username = usernameTextField.getText().trim();

            String encryptedPassword = getPasswordFromFile(name, username);
            if (encryptedPassword == null) {
                JOptionPane.showMessageDialog(null, "No matching password found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String decryptedPassword = EncryptionUtils.decrypt(encryptedPassword);
            PasswordController.copyPasswordToClipboard(decryptedPassword);
        });

        panel.add(copyPasswordButton);
    }

    private static String getPasswordFromFile(String service, String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PASSWORD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(service) && parts[1].equals(username)) {
                    return parts[2];
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading password file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
