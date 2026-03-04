package com.epam.gym.facade.trainee.mapper;

import com.epam.gym.domain.training.TrainingType;
import com.epam.gym.domain.user.Trainer;
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

class TrainerToTrainerProfileResponseMapperTest {

    private TrainerToTraineeProfileResponseMapper testObject;

    @BeforeEach
    void setUp() {
        testObject = Mappers.getMapper(TrainerToTraineeProfileResponseMapper.class);
    }

    private static Stream<Arguments> provideConvertData() {
        return Stream.of(
            Arguments.of("username1", "firstname1", "lastname1", "fitness"),
            Arguments.of("username2", "firstname2", "lastname2", "fitness")
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertData")
    void convert_shouldMapTrainerToShortResponse(String username, String fName, String lName, String specName) {
        var source = Trainer.builder()
            .username(username)
            .firstName(fName)
            .lastName(lName)
            .specialization(TrainingType.builder()
                .name(specName)
                .build())
            .build();

        var result = testObject.convert(source);

        assertNotNull(result);
        assertEquals(username, result.username());
        assertEquals(fName, result.firstName());
        assertEquals(lName, result.lastName());
        assertEquals(specName, result.specializationName());
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenSourceIsNull(Trainer source) {
        assertNull(testObject.convert(source));
    }
}
