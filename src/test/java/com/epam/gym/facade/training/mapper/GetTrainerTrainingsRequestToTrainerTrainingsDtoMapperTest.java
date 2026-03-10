package com.epam.gym.facade.training.mapper;

import com.epam.gym.controller.rest.training.dto.request.GetTrainerTrainingRequest;
import com.epam.gym.service.training.dto.TrainerTrainingsDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GetTrainerTrainingsRequestToTrainerTrainingsDtoMapperTest {

    private static final GetTrainerTrainingsRequestToTrainerTrainingsDtoMapper testObject =
        Mappers.getMapper(GetTrainerTrainingsRequestToTrainerTrainingsDtoMapper.class);

    private static Stream<Arguments> provideConvertData() {
        return Stream.of(
            Arguments.of(
                "trainer1",
                LocalDate.now().minusWeeks(2),
                LocalDate.now(),
                "traineeUsername"
            ),
            Arguments.of(
                "trainer2",
                null,
                null,
                null
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertData")
    void convert_shouldMapRequestToDto(String username, LocalDate from, LocalDate to, String traineeUsername) {
        var source = GetTrainerTrainingRequest.builder()
            .username(username)
            .from(from)
            .to(to)
            .traineeUsername(traineeUsername)
            .build();

        var result = testObject.convert(source);

        var expected = new TrainerTrainingsDto(username, from, to, traineeUsername);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenSourceIsNull(GetTrainerTrainingRequest source) {
        assertNull(testObject.convert(source));
    }
}
