package com.epam.gym.crm.facade.trainee.mapper;

import com.epam.gym.crm.controller.rest.trainee.dto.response.TrainerProfileResponse;
import com.epam.gym.crm.domain.training.TrainingType;
import com.epam.gym.crm.domain.user.Trainer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TrainerToTrainerProfileResponseMapperTest {

    private final TrainerToTraineeProfileResponseMapper testObject =
        Mappers.getMapper(TrainerToTraineeProfileResponseMapper.class);

    private static Stream<Arguments> provideConvertData() {
        return Stream.of(
            Arguments.of("username1", "firstname1", "lastname1", "fitness"),
            Arguments.of("username2", "firstname2", "lastname2", "fitness")
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertData")
    void convert_shouldMapTrainerToResponse(String username, String firstName, String lastName, String specializationName) {
        var source = Trainer.builder()
            .username(username)
            .firstName(firstName)
            .lastName(lastName)
            .specialization(TrainingType.builder()
                .name(specializationName)
                .build())
            .build();

        var actual = testObject.convert(source);

        var expected = new TrainerProfileResponse(username, firstName, lastName, specializationName);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenSourceIsNull(Trainer source) {
        assertNull(testObject.convert(source));
    }
}
