package com.epam.gym.mother;

public class UsernameMother {

    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";

    private static final String DEFAULT_USERNAME_DELIMITER = ".";

    public static String get() {
        return String.join(DEFAULT_USERNAME_DELIMITER,
            FIRSTNAME,
            LASTNAME);
    }

    public static String get(int suffix) {
        return String.join(DEFAULT_USERNAME_DELIMITER,
            FIRSTNAME,
            LASTNAME,
            String.valueOf(suffix));
    }
}
