package com.epam.gym.facade.training.mapper;

import com.epam.gym.controller.rest.training.dto.request.GetTrainerTrainingRequest;
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

class GetTrainerTrainingsRequestToTrainerTrainingsDtoMapperTest {

    private GetTrainerTrainingsRequestToTrainerTrainingsDtoMapper testObject;

    @BeforeEach
    void setUp() {
        testObject = Mappers.getMapper(GetTrainerTrainingsRequestToTrainerTrainingsDtoMapper.class);
    }

    private static Stream<Arguments> provideConvertData() {
        return Stream.of(
            Arguments.of("trainer1", LocalDate.now().minusWeeks(2), LocalDate.now(), "traineeUsername"),
            Arguments.of("trainer2", null, null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertData")
    void convert_shouldMapRequestToDto(String user, LocalDate from, LocalDate to, String trainee) {
        var source = GetTrainerTrainingRequest.builder()
            .username(user)
            .from(from)
            .to(to)
            .traineeUsername(trainee)
            .build();

        var result = testObject.convert(source);

        assertNotNull(result);
        assertEquals(user, result.username());
        assertEquals(from, result.from());
        assertEquals(to, result.to());
        assertEquals(trainee, result.traineeUsername());
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenSourceIsNull(GetTrainerTrainingRequest source) {
        assertNull(testObject.convert(source));
    }
}
