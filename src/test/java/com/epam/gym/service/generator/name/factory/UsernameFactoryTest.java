package com.epam.gym.service.generator.name.factory;

import com.epam.gym.mother.UsernameMother;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UsernameFactoryTest {

    private static final int SUFFIX_1 = 1;
    private static final int SUFFIX_2 = 2;
    private static final int BAD_SUFFIX_ZERO = 0;
    private static final int BAD_SUFFIX_NEGATIVE = -10;
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String USERNAME_WITHOUT_SUFFIX = UsernameMother.get();
    private static final String USERNAME_WITH_SUFFIX = UsernameMother.get(SUFFIX_2);
    private final IUsernameFactory testObject = new UsernameFactory();

    static Stream<Arguments> provideCreateUsernameTestData() {
        return Stream.of(
            Arguments.of(FIRSTNAME, LASTNAME, null, USERNAME_WITHOUT_SUFFIX),
            Arguments.of(FIRSTNAME, LASTNAME, SUFFIX_1, USERNAME_WITH_SUFFIX));
    }

    @ParameterizedTest
    @MethodSource("provideCreateUsernameTestData")
    void create_shouldReturnUsernameWithoutSuffix(String firstName, String lastName, Integer suffix, String username) {
        var result = Objects.isNull(suffix)
            ? testObject.create(firstName, lastName)
            : testObject.create(firstName, lastName, suffix);

        assertEquals(username, result);
    }

    private static Stream<Arguments> provideNullNameArgsForShortCreate() {
        return Stream.of(
            Arguments.of(null, LASTNAME),
            Arguments.of(FIRSTNAME, null),
            Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullNameArgsForShortCreate")
    void create_TwoArgs_shouldThrowNpe_whenNamesAreNull(String firstName, String lastName) {
        assertThrows(NullPointerException.class, () -> testObject.create(firstName, lastName));
    }

    private static Stream<Arguments> provideNullNameArgsWithSuffix() {
        return Stream.of(
            Arguments.of(null, LASTNAME, SUFFIX_1),
            Arguments.of(FIRSTNAME, null, SUFFIX_1),
            Arguments.of(null, null, SUFFIX_1),
            Arguments.of(null, LASTNAME, BAD_SUFFIX_NEGATIVE),
            Arguments.of(FIRSTNAME, null, BAD_SUFFIX_NEGATIVE),
            Arguments.of(null, null, BAD_SUFFIX_ZERO)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullNameArgsWithSuffix")
    void create_ThreeArgs_shouldThrowNpe_whenNamesAreNull(String firstName, String lastName, Integer suffix) {
        assertThrows(NullPointerException.class, () -> testObject.create(firstName, lastName, suffix));
    }

    static Stream<Arguments> provideBadSuffixesArguments() {
        return Stream.of(
            Arguments.of(BAD_SUFFIX_NEGATIVE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideBadSuffixesArguments")
    void shouldThrowIae_whenBadSuffix(int suffix) {
        assertThrows(IllegalArgumentException.class,
            () -> testObject.create(FIRSTNAME, LASTNAME, suffix));
    }
}
