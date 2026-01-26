package com.epam.gym.service.generator.name.supplier;

import com.epam.gym.GymApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompositeUsernameSupplierTest {

    private static final int SUFFIX_1 = 1;
    private static final String FIRSTNAME = "John";
    private static final String LASTNAME = "Doe";
    private static final String USERNAME_WITHOUT_SUFFIX = String.join(GymApplication.DEFAULT_USERNAME_DELIMITER,
        FIRSTNAME,
        LASTNAME);
    private static final String USERNAME_WITH_SUFFIX_1 = String.join(GymApplication.DEFAULT_USERNAME_DELIMITER,
        FIRSTNAME,
        LASTNAME,
        String.valueOf(SUFFIX_1));
    private static final List<String> EMPTY_USERNAME_LIST = List.of();
    private static final List<String> ONE_USERNAME_LIST = List.of(USERNAME_WITHOUT_SUFFIX);
    private static final List<String> MANY_USERNAMES_LIST = List.of(USERNAME_WITHOUT_SUFFIX, USERNAME_WITH_SUFFIX_1);
    public static final String EXCEPTION_MESSAGE = "Exception message";


    @Mock
    private IUsernameSupplier usernameSupplier;

    @InjectMocks
    private CompositeUsernameSupplier testObject;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(testObject,
            "usernameSuppliers",
            List.of(usernameSupplier));
    }

    static Stream<Arguments> provideSupplySingleSupplierTestData() {
        return Stream.of(
            Arguments.of(EMPTY_USERNAME_LIST),
            Arguments.of(ONE_USERNAME_LIST),
            Arguments.of(MANY_USERNAMES_LIST)
        );
    }

    @ParameterizedTest
    @MethodSource("provideSupplySingleSupplierTestData")
    void supply_singleSupplier(List<String> usernames) {
        when(usernameSupplier.supply(FIRSTNAME, LASTNAME))
            .thenReturn(usernames);

        var result = testObject.supply(FIRSTNAME, LASTNAME);

        assertEquals(usernames, result);
    }

    @Test
    void supply_singleSupplier_supplierThrowsExceptions() {
        when(usernameSupplier.supply(FIRSTNAME, LASTNAME))
            .thenThrow(new RuntimeException(EXCEPTION_MESSAGE));

        var exception = assertThrows(RuntimeException.class,
            () -> testObject.supply(FIRSTNAME, LASTNAME));
        assertEquals(EXCEPTION_MESSAGE, exception.getMessage());
    }

    static Stream<Arguments> provideSupplyNullParametersTestData() {
        return Stream.of(
            Arguments.of(null, LASTNAME),
            Arguments.of(FIRSTNAME, null),
            Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideSupplyNullParametersTestData")
    void supply_shouldThrowNpe_whenParametersAreNull(String firstName, String lastName) {
        assertThrows(NullPointerException.class,
            () -> testObject.supply(firstName, lastName));
    }
}
