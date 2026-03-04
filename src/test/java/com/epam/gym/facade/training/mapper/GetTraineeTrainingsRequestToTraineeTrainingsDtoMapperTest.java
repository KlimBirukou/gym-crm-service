package com.epam.gym.facade.training.mapper;

import com.epam.gym.controller.rest.training.dto.request.GetTraineeTrainingsRequest;
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

class GetTraineeTrainingsRequestToTraineeTrainingsDtoMapperTest {

    private GetTraineeTrainingsRequestToTraineeTrainingsDtoMapper testObject;

    @BeforeEach
    void setUp() {
        testObject = Mappers.getMapper(GetTraineeTrainingsRequestToTraineeTrainingsDtoMapper.class);
    }

    private static Stream<Arguments> provideConvertData() {
        return Stream.of(
            Arguments.of("trainee1", LocalDate.now().minusMonths(1), LocalDate.now(), "trainerUsername", "Yoga"),
            Arguments.of("trainee2", null, null, null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertData")
    void convert_shouldMapRequestToDto(String user, LocalDate from, LocalDate to, String trainer, String type) {
        var source = GetTraineeTrainingsRequest.builder()
            .username(user)
            .from(from)
            .to(to)
            .trainerUsername(trainer)
            .trainingTypeName(type)
            .build();

        var result = testObject.convert(source);

        assertNotNull(result);
        assertEquals(user, result.username());
        assertEquals(from, result.from());
        assertEquals(to, result.to());
        assertEquals(trainer, result.trainerUsername());
        assertEquals(type, result.trainingTypeName());
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenSourceIsNull(GetTraineeTrainingsRequest source) {
        assertNull(testObject.convert(source));
    }
}
