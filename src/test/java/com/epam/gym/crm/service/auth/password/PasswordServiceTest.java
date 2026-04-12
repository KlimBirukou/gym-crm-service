package com.epam.gym.crm.service.auth.password;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PasswordServiceTest {

    private static final String PLAIN_PASSWORD = "plain_password";
    private static final String WRONG_PASSWORD = "wrong_password";

    private final PasswordService testObject = new PasswordService();

    @Test
    void hashPassword_shouldReturnHashedString_whenPasswordProvided() {
        var result = testObject.hashPassword(PLAIN_PASSWORD);

        assertNotNull(result);
        assertNotEquals(PLAIN_PASSWORD, result);
    }

    @ParameterizedTest
    @NullSource
    void hashPassword_shouldThrowException_whenPasswordNull(String password) {
        assertThrows(NullPointerException.class, () -> testObject.hashPassword(password));
    }

    @Test
    void checkPassword_shouldReturnFalse_whenPasswordsDoNotMatch() {
        var hash = testObject.hashPassword(PLAIN_PASSWORD);

        var result = testObject.checkPassword(WRONG_PASSWORD, hash);

        assertFalse(result);
    }

    private static Stream<Arguments> provideNullArguments() {
        return Stream.of(
            Arguments.of(null, PLAIN_PASSWORD),
            Arguments.of(PLAIN_PASSWORD, null),
            Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullArguments")
    void checkPassword_shouldThrowException_whenArgumentsNull(String password, String hash) {
        assertThrows(NullPointerException.class,
            () -> testObject.checkPassword(password, hash));
    }
}
