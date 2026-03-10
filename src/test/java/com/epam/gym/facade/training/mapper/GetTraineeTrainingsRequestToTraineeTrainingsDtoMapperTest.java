package com.epam.gym.facade.training.mapper;

import com.epam.gym.controller.rest.training.dto.request.GetTraineeTrainingsRequest;
import com.epam.gym.service.training.dto.TraineeTrainingsDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GetTraineeTrainingsRequestToTraineeTrainingsDtoMapperTest {

    private final GetTraineeTrainingsRequestToTraineeTrainingsDtoMapper testObject =
        Mappers.getMapper(GetTraineeTrainingsRequestToTraineeTrainingsDtoMapper.class);

    private static Stream<Arguments> provideConvertData() {
        return Stream.of(
            Arguments.of(
                "trainee1",
                LocalDate.now().minusMonths(1),
                LocalDate.now(),
                "trainerUsername",
                "Yoga"
            ),
            Arguments.of(
                "trainee2",
                null,
                null,
                null,
                null
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertData")
    void convert_shouldMapRequestToDto(String username, LocalDate from, LocalDate to,
                                       String trainerUsername, String trainingTypeName) {
        var source = GetTraineeTrainingsRequest.builder()
            .username(username)
            .from(from)
            .to(to)
            .trainerUsername(trainerUsername)
            .trainingTypeName(trainingTypeName)
            .build();

        var result = testObject.convert(source);

        var expected = new TraineeTrainingsDto(username, from, to, trainerUsername, trainingTypeName);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenSourceIsNull(GetTraineeTrainingsRequest source) {
        assertNull(testObject.convert(source));
    }
}
