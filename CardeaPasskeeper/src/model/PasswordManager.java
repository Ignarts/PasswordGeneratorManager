package model;

import java.util.Random;

public class PasswordManager {
    private static final String LOWER_CASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_CASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGIT_CHARACTERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()_+";
    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 30;
    private static final int DEFAULT_LENGTH = 30;

    public static String generatePassword(int length, boolean upperCase, boolean digit, boolean special) {
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        int passwordLength  = length;
        if(length < MIN_LENGTH || length > MAX_LENGTH) {
            passwordLength = DEFAULT_LENGTH;
        }

        String possibleValues = possiblePasswordValues(upperCase, digit, special);

        for (int i = 0; i < passwordLength; i++) {
            int index = random.nextInt(possibleValues.length());
            password.append(possibleValues.charAt(index));
        }

        return password.toString();
    }

    private static String possiblePasswordValues(boolean upperCase, boolean digit, boolean special) {
        String possibleValues = LOWER_CASE_CHARACTERS;

        if(upperCase)
            possibleValues += UPPER_CASE_CHARACTERS;
        if(digit)
            possibleValues += DIGIT_CHARACTERS;
        if(special)
            possibleValues += SPECIAL_CHARACTERS;

        return possibleValues;
    }
}