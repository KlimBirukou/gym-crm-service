package com.epam.gym.facade.training.mapper;

import com.epam.gym.controller.rest.training.dto.request.CreateTrainingRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CreateTrainingRequestToCreateTrainingDtoMapperTest {

    private CreateTrainingRequestToCreateTrainingDtoMapper testObject;

    @BeforeEach
    void setUp() {
        testObject = Mappers.getMapper(CreateTrainingRequestToCreateTrainingDtoMapper.class);
    }

    private static Stream<Arguments> provideConvertData() {
        return Stream.of(
            Arguments.of("teUsername", "trUsername", "name", LocalDate.now(), 60, "Yoga"),
            Arguments.of("teUsername", "trUsername", "name", LocalDate.now().plusDays(1), 90, "Fitness")
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertData")
    void convert_shouldMapRequestToDto(String trainee, String trainer, String name, LocalDate date, Integer duration) {
        var source = CreateTrainingRequest.builder()
            .traineeUsername(trainee)
            .trainerUsername(trainer)
            .name(name)
            .date(date)
            .durationInMinutes(duration)
            .build();

        var result = testObject.convert(source);

        assertNotNull(result);
        assertEquals(trainee, result.traineeUsername());
        assertEquals(trainer, result.trainerUsername());
        assertEquals(name, result.name());
        assertEquals(date, result.date());
        assertEquals(duration, result.durationInMinutes());
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenSourceIsNull(CreateTrainingRequest source) {
        assertNull(testObject.convert(source));
    }
}
