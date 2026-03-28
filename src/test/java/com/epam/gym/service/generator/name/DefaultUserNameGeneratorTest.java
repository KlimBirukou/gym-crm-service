package com.epam.gym.service.generator.name;

import com.epam.gym.configuration.properties.UserProperties;
import com.epam.gym.service.generator.name.factory.IUsernameFactory;
import com.epam.gym.service.generator.name.supplier.IUsernameSupplier;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultUserNameGeneratorTest {

    private static final int SUFFIX_0 = 0;
    private static final int SUFFIX_1 = 1;
    private static final int SUFFIX_2 = 2;
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String DEFAULT_USERNAME_DELIMITER = ".";
    private static final String USERNAME_WITHOUT_SUFFIX = buildUsername();
    private static final String USERNAME_WITH_SUFFIX_1 = buildUsername(SUFFIX_1);
    private static final String USERNAME_WITH_SUFFIX_2 = buildUsername(SUFFIX_2);
    private static final List<String> EMPTY_USERNAME_LIST = List.of();
    private static final List<String> ONE_USERNAME_LIST = List.of(USERNAME_WITHOUT_SUFFIX);
    private static final List<String> MANY_USERNAMES_LIST = List.of(USERNAME_WITHOUT_SUFFIX, USERNAME_WITH_SUFFIX_1);
    private static final String BLANK = StringUtils.SPACE;

    @Mock
    private IUsernameFactory usernameFactory;
    @Mock
    private IUsernameSupplier usernameSupplier;
    @Mock
    private UserProperties userProperties;

    @InjectMocks
    private DefaultUsernameGenerator testObject;

    @BeforeEach
    void setUp() {
        lenient().doReturn(DEFAULT_USERNAME_DELIMITER).when(userProperties).usernameDelimiter();
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(usernameFactory, usernameSupplier);
    }

    static Stream<Arguments> provideGenerateWithEmptySupplierTestData() {
        return Stream.of(
            Arguments.of(FIRSTNAME, LASTNAME, USERNAME_WITHOUT_SUFFIX)
        );
    }

    @ParameterizedTest
    @MethodSource("provideGenerateWithEmptySupplierTestData")
    void generate_shouldReturnExpectedResult_whenSupplierReturnEmptyList(
        String firstName,
        String lastName,
        String username
    ) {
        when(usernameSupplier.supply(firstName, lastName)).thenReturn(EMPTY_USERNAME_LIST);
        when(usernameFactory.create(firstName, lastName)).thenReturn(username);

        var result = testObject.generate(firstName, lastName);

        assertEquals(result, username);
        verify(usernameSupplier).supply(firstName, lastName);
        verify(usernameFactory).create(firstName, lastName);
    }

    static Stream<Arguments> provideGenerateWithNotEmptySupplierTestData() {
        return Stream.of(
            Arguments.of(FIRSTNAME, LASTNAME, ONE_USERNAME_LIST, SUFFIX_0, USERNAME_WITH_SUFFIX_1),
            Arguments.of(FIRSTNAME, LASTNAME, MANY_USERNAMES_LIST, SUFFIX_1, USERNAME_WITH_SUFFIX_2)
        );
    }

    @ParameterizedTest
    @MethodSource("provideGenerateWithNotEmptySupplierTestData")
    void generate_shouldReturnExpectedResult_whenSupplierReturnNotEmptyList(
        String firstName,
        String lastName,
        List<String> usernamesList,
        int calculatedSuffix,
        String username
    ) {
        when(usernameSupplier.supply(firstName, lastName)).thenReturn(usernamesList);
        when(usernameFactory.create(firstName, lastName, calculatedSuffix)).thenReturn(username);

        var result = testObject.generate(firstName, lastName);

        assertEquals(result, username);
        verify(usernameSupplier).supply(firstName, lastName);
    }

    static Stream<Arguments> provideNullNameArguments() {
        return Stream.of(
            Arguments.of(null, LASTNAME),
            Arguments.of(FIRSTNAME, null),
            Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullNameArguments")
    void shouldThrowNpe_whenParametersAreNull(String firstName, String lastName) {
        assertThrows(NullPointerException.class,
            () -> testObject.generate(firstName, lastName));
    }

    static Stream<Arguments> provideBlankNameArguments() {
        return Stream.of(
            Arguments.of(BLANK, LASTNAME),
            Arguments.of(FIRSTNAME, BLANK),
            Arguments.of(BLANK, BLANK)
        );
    }

    @ParameterizedTest
    @MethodSource("provideBlankNameArguments")
    void shouldThrowIae_whenParametersAreBlank(String firstName, String lastName) {
        assertThrows(IllegalArgumentException.class,
            () -> testObject.generate(firstName, lastName));
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
