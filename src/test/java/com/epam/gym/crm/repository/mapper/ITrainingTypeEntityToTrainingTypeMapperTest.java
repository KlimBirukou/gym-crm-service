package com.epam.gym.crm.repository.mapper;

import com.epam.gym.crm.repository.entity.TrainingTypeEntity;
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

class ITrainingTypeEntityToTrainingTypeMapperTest {

    private final ITrainingTypeEntityToTrainingTypeMapper testObject =
        Mappers.getMapper(ITrainingTypeEntityToTrainingTypeMapper.class);

    private static Stream<Arguments> provideConvertData() {
        return Stream.of(
            Arguments.of(UUID.randomUUID(), "Karate"),
            Arguments.of(UUID.randomUUID(), "Yoga"),
            Arguments.of(UUID.randomUUID(), "Powerlifting")
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertData")
    void convert_shouldMapEntityToDomain(UUID uid, String name) {
        var entity = TrainingTypeEntity.builder()
            .uid(uid)
            .name(name)
            .build();

        var result = testObject.convert(entity);

        assertNotNull(result);
        assertEquals(uid, result.getUid());
        assertEquals(name, result.getName());
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenArgumentNull(TrainingTypeEntity entity) {
        var result = testObject.convert(entity);

        assertNull(result);
    }
}
