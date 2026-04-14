package com.epam.gym.crm.service.generator.name.factory;

import com.epam.gym.crm.configuration.properties.UserProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class UsernameFactoryTest {

    private static final String USERNAME_DELIMITER = ".";
    private static final int SUFFIX_1 = 1;
    private static final int SUFFIX_2 = 2;
    private static final int BAD_SUFFIX_ZERO = 0;
    private static final int BAD_SUFFIX_NEGATIVE = -10;
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String DEFAULT_USERNAME_DELIMITER = ".";
    private static final String USERNAME_WITHOUT_SUFFIX =buildUsername();
    private static final String USERNAME_WITH_SUFFIX = buildUsername(SUFFIX_2);

    @Mock
    private UserProperties userProperties;

    @InjectMocks
    private UsernameFactory testObject;

    @BeforeEach
    void setUp() {
        lenient().doReturn(USERNAME_DELIMITER).when(userProperties).usernameDelimiter();
    }

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

    private static String buildUsername() {
        return String.join(DEFAULT_USERNAME_DELIMITER,
            FIRSTNAME,
            LASTNAME);
    }

    private static String buildUsername(int suffix) {
        return String.join(DEFAULT_USERNAME_DELIMITER,
            FIRSTNAME,
            LASTNAME,
            String.valueOf(suffix));
    }
}
