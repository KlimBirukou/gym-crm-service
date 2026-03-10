package com.epam.gym.facade.training.mapper;

import com.epam.gym.controller.rest.training.dto.response.TrainingTypeResponse;
import com.epam.gym.domain.training.TrainingType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ITrainingTypeToTrainingTypeResponseMapperTest {

    private final ITrainingTypeToTrainingTypeResponseMapper testObject =
        Mappers.getMapper(ITrainingTypeToTrainingTypeResponseMapper.class);

    private static Stream<Arguments> provideConvertData() {
        return Stream.of(
            Arguments.of(UUID.randomUUID(), "Yoga"),
            Arguments.of(UUID.randomUUID(), "Fitness")
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

        var expected = new TrainingTypeResponse(uid, name);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenSourceIsNull(TrainingType source) {
        assertNull(testObject.convert(source));
    }
}
