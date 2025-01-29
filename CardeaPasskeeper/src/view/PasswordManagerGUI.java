package view;

import controller.PasswordController;
import model.PasswordEntry;
import model.PasswordManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class PasswordManagerGUI {
    public static PasswordController controller = new PasswordController();

    private static JTextField applicationNameTextField;
    private static JTextField usernameTextField;
    private static JTable passwordTable;
    private static DefaultTableModel tableModel;

    public static void runApplication() {
        JFrame frame = new JFrame("Cardea Passkeeper");
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(44, 62, 80));

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        createPasswordTable(tablePanel);
        frame.add(tablePanel, BorderLayout.CENTER);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBackground(new Color(52, 73, 94));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        createFormFields(formPanel);
        frame.add(formPanel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(52, 73, 94));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        createButtons(buttonPanel);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setSize(750, 550);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void createPasswordTable(JPanel panel) {
        String[] columnNames = {"Service", "Username", "Password"};
        tableModel = new DefaultTableModel(columnNames, 0);
        passwordTable = new JTable(tableModel);
        passwordTable.setRowHeight(30);
        passwordTable.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordTable.setBackground(new Color(236, 240, 241));

        JScrollPane scrollPane = new JScrollPane(passwordTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        refreshTable();
    }

    private static void createFormFields(JPanel panel) {
        applicationNameTextField = new JTextField(15);
        usernameTextField = new JTextField(15);

        JLabel serviceLabel = new JLabel("Service:");
        serviceLabel.setForeground(Color.WHITE);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);

        panel.add(serviceLabel);
        panel.add(applicationNameTextField);
        panel.add(usernameLabel);
        panel.add(usernameTextField);
    }

    private static void createButtons(JPanel panel) {
        JButton addPasswordButton = new JButton("Add Password");
        JButton removePasswordButton = new JButton("Remove Password");
        JButton copyPasswordButton = new JButton("Copy Password");

        styleButton(addPasswordButton, new Color(46, 204, 113));
        styleButton(removePasswordButton, new Color(231, 76, 60));
        styleButton(copyPasswordButton, new Color(52, 152, 219));

        addPasswordButton.addActionListener(e -> addPassword());
        removePasswordButton.addActionListener(e -> removePassword());
        copyPasswordButton.addActionListener(e -> copyPassword());

        panel.add(addPasswordButton);
        panel.add(removePasswordButton);
        panel.add(copyPasswordButton);
    }

    private static void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }

    private static void addPassword() {
        String name = applicationNameTextField.getText().trim();
        String username = usernameTextField.getText().trim();

        if (name.isEmpty() || username.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter Service and Username", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PasswordEntry entry = new PasswordEntry(
                name,
                username,
                PasswordManager.generatePassword(16, true, true, true)
        );
        controller.addPassword(entry);
        refreshTable();
    }

    private static void removePassword() {
        int selectedRow = passwordTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Select a password to remove", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String service = tableModel.getValueAt(selectedRow, 0).toString();
        String username = tableModel.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this password?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.removePassword(service, username);
            refreshTable();
        }
    }

    private static void copyPassword() {
        int selectedRow = passwordTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Select a password to copy", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String encryptedPassword = tableModel.getValueAt(selectedRow, 2).toString();
        PasswordController.copyPasswordToClipboard(encryptedPassword);
    }

    private static void refreshTable() {
        tableModel.setRowCount(0);
        for (PasswordEntry entry : controller.getPasswords()) {
            tableModel.addRow(new Object[]{entry.getService(), entry.getUsername(), "********"});
        }
    }
}