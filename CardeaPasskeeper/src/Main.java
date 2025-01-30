import controller.UserController;
import model.EncryptionUtils;
import view.PasswordManagerGUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        if (!UserController.isUserRegistered()) {
            UserController.createUser();
        }

        boolean authenticated = false;
        while (!authenticated) {
            String password = JOptionPane.showInputDialog("Enter your password:");
            if (password == null) return;

            if (UserController.validateUser(password)) {
                authenticated = true;
                EncryptionUtils.initializeEncryption(); // Inicializamos la clave de cifrado
                PasswordManagerGUI.runApplication();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid password. Try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
