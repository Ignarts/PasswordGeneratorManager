package model;

import java.util.Random;

public class PasswordManager {
    private static final String lowerCaseCharacters = "abcdefghijklmnopqrstuvwxyz";
    private static final String upperCaseCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String digitCharacters = "0123456789";
    private static final String specialCharacters = "!@#$%^&*()_+";

    public static String generatePassword(int length, boolean upperCase, boolean digit, boolean special) {
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        String posibleValues = lowerCaseCharacters;

        if(upperCase)
            posibleValues += upperCaseCharacters;
        if(digit)
            posibleValues += digitCharacters;
        if(special)
            posibleValues += specialCharacters;

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(posibleValues.length());
            password.append(posibleValues.charAt(index));
        }

        return password.toString();
    }
}