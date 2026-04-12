package com.epam.gym.crm.service.generator.password;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultPasswordGeneratorTest {

    private static final int EXPECTED_LENGTH = 10;
    private static final String PASSWORD_PATTERN = "[0-9a-f]+";

    private final IPasswordGenerator testObject = new DefaultPasswordGenerator();

    @Test
    void generate_shouldReturnNonNullPassword() {
        var result = testObject.generate();

        assertNotNull(result);
    }

    @Test
    void generate_shouldReturnPasswordWithExpectedLength() {
        var result = testObject.generate();

        assertEquals(EXPECTED_LENGTH, result.length());
    }

    @Test
    void generate_shouldReturnPasswordWithAllowedCharactersOnly() {
        var result = testObject.generate();

        assertTrue(result.matches(PASSWORD_PATTERN));
    }

    @Test
    void generate_shouldGenerateDifferentPasswordsOnEachCall() {
        var first = testObject.generate();
        var second = testObject.generate();

        assertNotEquals(first, second);
    }
}
