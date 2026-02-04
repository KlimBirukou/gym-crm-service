package com.epam.gym.mother;

import com.epam.gym.GymApplication;

public class UsernameMother {

    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";

    public static String get() {
        return String.join(GymApplication.DEFAULT_USERNAME_DELIMITER,
            FIRSTNAME,
            LASTNAME);
    }

    public static String get(int suffix) {
        return String.join(GymApplication.DEFAULT_USERNAME_DELIMITER,
            FIRSTNAME,
            LASTNAME,
            String.valueOf(suffix));
    }
}
