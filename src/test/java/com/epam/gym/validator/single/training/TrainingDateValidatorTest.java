package com.epam.gym.validator.single.training;

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

class TrainingDateValidatorTest {

    private static final String FIXED_NOW_ISO = "2026-01-01T00:00:00Z";
    private static final ZoneId ZONE = ZoneId.of("UTC");
    private static final LocalDate TODAY = LocalDate.of(2026, 1, 1);
    private static final int MIN_DAYS = 3;
    private static final int MAX_MONTHS = 6;
    private static final LocalDate VALID_DATE = TODAY.plusMonths(2);
    private static final LocalDate MIN_BOUNDARY = TODAY.plusDays(MIN_DAYS);
    private static final LocalDate MAX_BOUNDARY = TODAY.plusMonths(MAX_MONTHS);
    private static final LocalDate PAST_DATE = TODAY.minusDays(1);
    private static final LocalDate TOO_EARLY = TODAY.plusDays(MIN_DAYS).minusDays(1);
    private static final LocalDate TOO_FAR = TODAY.plusMonths(MAX_MONTHS).plusDays(1);

    private TrainingDateValidator testObject;

    @BeforeEach
    void setUp() {
        var clock = Clock.fixed(Instant.parse(FIXED_NOW_ISO), ZONE);
        testObject = new TrainingDateValidator(clock, MIN_DAYS, MAX_MONTHS);
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
            PAST_DATE,
            TOO_EARLY,
            TOO_FAR
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
