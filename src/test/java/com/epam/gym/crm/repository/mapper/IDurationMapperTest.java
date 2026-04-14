package com.epam.gym.crm.repository.mapper;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;

import java.time.Duration;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class IDurationMapperTest {

    private final IDurationMapper testObject = Mappers.getMapper(IDurationMapper.class);

    private static Stream<Arguments> provideMinutesToDurationData() {
        return Stream.of(
            Arguments.of(60, Duration.ofMinutes(60)),
            Arguments.of(0, Duration.ZERO),
            Arguments.of(1440, Duration.ofHours(24))
        );
    }

    @ParameterizedTest
    @MethodSource("provideMinutesToDurationData")
    void toDuration_shouldConvertMinutesToDuration(Integer input, Duration expected) {
        var result = testObject.toDuration(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @NullSource
    void toDuration_shouldReturnNull_whenArgumentNull(Integer minutes) {
        var result = testObject.toDuration(minutes);

        assertNull(result);
    }

    private static Stream<Arguments> provideDurationToMinutesData() {
        return Stream.of(
            Arguments.of(Duration.ofMinutes(90), 90),
            Arguments.of(Duration.ZERO, 0),
            Arguments.of(Duration.ofHours(2), 120),
            Arguments.of(Duration.ofSeconds(150), 2)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDurationToMinutesData")
    void toMinutes_shouldConvertDurationToMinutes_whenValidInput(Duration input, Integer expected) {
        var result = testObject.toMinutes(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @NullSource
    void toMinutes_shouldReturnNull_whenArgumentNull(Duration duration) {
        var result = testObject.toMinutes(duration);

        assertNull(result);
    }
}
