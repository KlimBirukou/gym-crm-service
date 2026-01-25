package com.epam.gym.service.generator.name.factory;

import com.epam.gym.GymApplication;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UsernameFactoryTest {

    private static final int PREV_SUFFIX = 2;
    private static final int NEW_SUFFIX = 3;
    private static final int BAD_SUFFIX = -1;
    private static final String FIRSTNAME = "John";
    private static final String LASTNAME = "Doe";
    private static final String EXPECTED_WITHOUT_SUFFIX = FIRSTNAME
        + GymApplication.DEFAULT_USERNAME_DELIMITER
        + LASTNAME;
    private static final String EXPECTED_WITH_SUFFIX = FIRSTNAME
        + GymApplication.DEFAULT_USERNAME_DELIMITER
        + LASTNAME
        + GymApplication.DEFAULT_USERNAME_DELIMITER
        + NEW_SUFFIX;

    private final IUsernameFactory testObject = new UsernameFactory();

    @Test
    void create_shouldCreateUsernameWithoutSuffix() {
        var result = testObject.create(FIRSTNAME, LASTNAME);

        assertEquals(EXPECTED_WITHOUT_SUFFIX, result);
    }

    @Test
    void create_shouldCreateUsernameWithSuffix() {
        var result = testObject.create(FIRSTNAME, LASTNAME, PREV_SUFFIX);

        assertEquals(EXPECTED_WITH_SUFFIX, result);
    }

    @Test
    void create_shouldThrowNPE() {
        assertThrows(NullPointerException.class,
            () -> testObject.create(FIRSTNAME, null));
        assertThrows(NullPointerException.class,
            () -> testObject.create(null, LASTNAME));
        assertThrows(NullPointerException.class,
            () -> testObject.create(null, null));
        assertThrows(NullPointerException.class,
            () -> testObject.create(FIRSTNAME, null, PREV_SUFFIX));
        assertThrows(NullPointerException.class,
            () -> testObject.create(null, LASTNAME, PREV_SUFFIX));
        assertThrows(NullPointerException.class,
            () -> testObject.create(null, null, PREV_SUFFIX));
    }

    @Test
    void create_shouldThrowIAE() {
        assertThrows(IllegalArgumentException.class,
            () -> testObject.create(FIRSTNAME, LASTNAME, BAD_SUFFIX));
    }
}
