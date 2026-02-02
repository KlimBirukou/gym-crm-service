package com.epam.gym.validator.single.trainee;

import com.epam.gym.exception.DateValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TraineeBirthdateValidatorTest {

    private static final String FIXED_NOW_ISO = "2026-01-01T00:00:00Z";
    private static final ZoneId ZONE = ZoneId.of("UTC");

    private static final LocalDate TODAY = LocalDate.of(2026, 1, 1);
    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 60;
    private static final LocalDate VALID_DATE = TODAY.minusYears(25);
    private static final LocalDate MIN_BOUNDARY = TODAY.minusYears(MIN_AGE);
    private static final LocalDate MAX_BOUNDARY = TODAY.minusYears(MAX_AGE);
    private static final LocalDate FUTURE_DATE = TODAY.plusDays(1);
    private static final LocalDate TOO_YOUNG = TODAY.minusYears(MIN_AGE).plusDays(1);
    private static final LocalDate TOO_OLD = TODAY.minusYears(MAX_AGE).minusDays(1);

    private TraineeBirthdateValidator testObject;

    @BeforeEach
    void setUp() {
        var clock = Clock.fixed(Instant.parse(FIXED_NOW_ISO), ZONE);
        testObject = new TraineeBirthdateValidator(clock, MIN_AGE, MAX_AGE);
    }

    private static Stream<LocalDate> provideValidDates() {
        return Stream.of(
            VALID_DATE,
            MIN_BOUNDARY,
            MAX_BOUNDARY
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidDates")
    void validate_shouldPass_whenDateValid(LocalDate date) {
        assertDoesNotThrow(() -> testObject.validate(date));
    }

    private static Stream<LocalDate> provideInvalidDates() {
        return Stream.of(
            FUTURE_DATE,
            TOO_YOUNG,
            TOO_OLD
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDates")
    void validate_shouldThrowException_whenDateInvalid(LocalDate date) {
        assertThrows(DateValidationException.class,
            () -> testObject.validate(date));
    }

    @ParameterizedTest
    @NullSource
    void validate_shouldThrowException_whenDateNull(LocalDate date) {
        assertThrows(NullPointerException.class,
            () -> testObject.validate(date));
    }
}
