package com.epam.gym.service.generator.name.factory;

import com.epam.gym.GymApplication;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UsernameFactoryTest {

    private static final int SUFFIX_1 = 1;
    private static final int SUFFIX_2 = 2;
    private static final int BAD_SUFFIX = 0;
    private static final String FIRSTNAME = "John";
    private static final String LASTNAME = "Doe";
    private static final String USERNAME_WITHOUT_SUFFIX = String.join(
        GymApplication.DEFAULT_USERNAME_DELIMITER,
        FIRSTNAME,
        LASTNAME);
    private static final String USERNAME_WITH_SUFFIX = String.join(
        GymApplication.DEFAULT_USERNAME_DELIMITER,
        FIRSTNAME,
        LASTNAME,
        String.valueOf(SUFFIX_2));

    private final IUsernameFactory testObject = new UsernameFactory();

    @Test
    void create_shouldReturnUsernameWithoutSuffix() {
        var result = testObject.create(FIRSTNAME, LASTNAME);

        assertEquals(USERNAME_WITHOUT_SUFFIX, result);
    }

    @Test
    void create_shouldReturnUsernameWithSuffix() {
        var result = testObject.create(FIRSTNAME, LASTNAME, SUFFIX_1);

        assertEquals(USERNAME_WITH_SUFFIX, result);
    }

    @Test
    void create_shouldThrowNpe_whenLastNameIsNull() {
        assertThrows(NullPointerException.class,
            () -> testObject.create(FIRSTNAME, null));
    }

    @Test
    void create_shouldThrowNpe_whenFirstNameIsNull() {
        assertThrows(NullPointerException.class,
            () -> testObject.create(null, LASTNAME));
    }

    @Test
    void create_shouldThrowNpe_whenBothNameAreNull() {
        assertThrows(NullPointerException.class,
            () -> testObject.create(null, null));
    }

    @Test
    void create_shouldThrowNpe_whenFirstNameIsNullWithSuffix() {
        assertThrows(NullPointerException.class,
            () -> testObject.create(null, LASTNAME, SUFFIX_1));
    }

    @Test
    void create_shouldThrowNpe_whenLastNameIsNullWithSuffix() {
        assertThrows(NullPointerException.class,
            () -> testObject.create(FIRSTNAME, null, SUFFIX_1));
    }

    @Test
    void create_shouldThrowNpe_whenBothNameAreNullWithSuffix() {
        assertThrows(NullPointerException.class,
            () -> testObject.create(null, null, SUFFIX_1));
    }

    @Test
    void create_shouldThrowIae_whenBadSuffix() {
        assertThrows(IllegalArgumentException.class,
            () -> testObject.create(FIRSTNAME, LASTNAME, BAD_SUFFIX));
    }
}
