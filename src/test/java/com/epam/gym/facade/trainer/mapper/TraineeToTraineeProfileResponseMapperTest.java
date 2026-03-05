package com.epam.gym.facade.trainer.mapper;

import com.epam.gym.controller.rest.trainer.dto.response.TraineeProfileResponse;
import com.epam.gym.domain.user.Trainee;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TraineeToTraineeProfileResponseMapperTest {

    private final TraineeToTraineeProfileResponseMapper testObject =
        Mappers.getMapper(TraineeToTraineeProfileResponseMapper.class);

    private static Stream<Arguments> provideConvertData() {
        return Stream.of(
            Arguments.of("username1", "firstname1", "lastname1"),
            Arguments.of("username2", "firstname2", "lastname2")
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertData")
    void convert_shouldMapTraineeToResponse(String username, String firstName, String lastName) {
        var source = Trainee.builder()
            .username(username)
            .firstName(firstName)
            .lastName(lastName)
            .build();

        var actual = testObject.convert(source);

        var expected = new TraineeProfileResponse(username, firstName, lastName);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenSourceIsNull(Trainee source) {
        assertNull(testObject.convert(source));
    }
}
