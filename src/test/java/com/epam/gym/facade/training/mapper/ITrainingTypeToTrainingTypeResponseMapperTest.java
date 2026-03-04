package com.epam.gym.facade.training.mapper;

import com.epam.gym.domain.training.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ITrainingTypeToTrainingTypeResponseMapperTest {

    private static final UUID UID_1 = UUID.randomUUID();
    private static final UUID UID_2 = UUID.randomUUID();
    private static final String TRAINING_TYPE_NAME_1 = "Yoga";
    private static final String TRAINING_TYPE_NAME_2 = "Fitness";

    private ITrainingTypeToTrainingTypeResponseMapper testObject;

    @BeforeEach
    void setUp() {
        testObject = Mappers.getMapper(ITrainingTypeToTrainingTypeResponseMapper.class);
    }

    private static Stream<Arguments> provideConvertData() {
        return Stream.of(
            Arguments.of(UID_1, TRAINING_TYPE_NAME_1),
            Arguments.of(UID_2, TRAINING_TYPE_NAME_2)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertData")
    void convert_shouldMapTrainingTypeToResponse(UUID uid, String name) {
        var source = TrainingType.builder()
            .uid(uid)
            .name(name)
            .build();

        var result = testObject.convert(source);

        assertNotNull(result);
        assertEquals(uid, result.uid());
        assertEquals(name, result.name());
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenSourceIsNull(TrainingType source) {
        assertNull(testObject.convert(source));
    }
}
