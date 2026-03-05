package com.epam.gym.facade.training.mapper;

import com.epam.gym.controller.rest.training.dto.request.CreateTrainingRequest;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CreateTrainingRequestToCreateTrainingDtoMapperTest {

    private CreateTrainingRequestToCreateTrainingDtoMapper testObject =
        Mappers.getMapper(CreateTrainingRequestToCreateTrainingDtoMapper.class);

    private static Stream<Arguments> provideConvertData() {
        return Stream.of(
            Arguments.of(
                "teUsername",
                "trUsername",
                "name",
                LocalDate.now(),
                60,
                "Yoga"
            ),
            Arguments.of(
                "teUsername",
                "trUsername",
                "name",
                LocalDate.now().plusDays(1),
                90,
                "Fitness"
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertData")
    void convert_shouldMapRequestToDto(String traineeUsername, String trainerUsername,
                                       String name, LocalDate date, Integer durationInMinutes) {
        var source = CreateTrainingRequest.builder()
            .traineeUsername(traineeUsername)
            .trainerUsername(trainerUsername)
            .name(name)
            .date(date)
            .durationInMinutes(durationInMinutes)
            .build();

        var actual = testObject.convert(source);

        var expected = new CreateTrainingDto(traineeUsername, trainerUsername, name, date, durationInMinutes);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenSourceIsNull(CreateTrainingRequest source) {
        assertNull(testObject.convert(source));
    }
}
