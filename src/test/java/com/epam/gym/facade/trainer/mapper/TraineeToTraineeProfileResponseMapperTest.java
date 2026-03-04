package com.epam.gym.facade.trainer.mapper;

import com.epam.gym.domain.user.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TraineeToTraineeProfileResponseMapperTest {

    private TraineeToTraineeProfileResponseMapper testObject;

    @BeforeEach
    void setUp() {
        testObject = Mappers.getMapper(TraineeToTraineeProfileResponseMapper.class);
    }

    private static Stream<Arguments> provideConvertData() {
        return Stream.of(
            Arguments.of("username1", "firstname1", "lastname1"),
            Arguments.of("username2", "firstname2", "lastname2")
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertData")
    void convert_shouldMapTraineeToShortResponse(String username, String fName, String lName) {
        var source = Trainee.builder()
            .username(username)
            .firstName(fName)
            .lastName(lName)
            .build();

        var result = testObject.convert(source);

        assertNotNull(result);
        assertEquals(username, result.username());
        assertEquals(fName, result.firstName());
        assertEquals(lName, result.lastName());
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenSourceIsNull(Trainee source) {
        assertNull(testObject.convert(source));
    }
}
