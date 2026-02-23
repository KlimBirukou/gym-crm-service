package com.epam.gym.repository.mapper;

import com.epam.gym.domain.training.TrainingType;
import com.epam.gym.repository.entity.TrainingTypeEntity;
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

class ITrainingTypeEntityToTrainingTypeMapperTest {

    private ITrainingTypeEntityToTrainingTypeMapper testObject;

    @BeforeEach
    void setUp() {
        testObject = Mappers.getMapper(ITrainingTypeEntityToTrainingTypeMapper.class);
    }

    private static Stream<Arguments> provideConvertData() {
        return Stream.of(
            Arguments.of(UUID.randomUUID(), "CARDIO"),
            Arguments.of(UUID.randomUUID(), "YOGA"),
            Arguments.of(UUID.randomUUID(), "STRENGTH")
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
    void convert_shouldThrowException_whenArgumentNull(TrainingTypeEntity entity) {
        var result = testObject.convert(entity);

        assertNull(result);
    }


    private static Stream<Arguments> provideInvertConvertData() {
        return Stream.of(
            Arguments.of(UUID.randomUUID(), "CARDIO"),
            Arguments.of(UUID.randomUUID(), "YOGA"),
            Arguments.of(UUID.randomUUID(), "STRENGTH")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvertConvertData")
    void invertConvert_shouldMapDomainToEntity(UUID uid, String name) {
        var domain = TrainingType.builder()
            .uid(uid)
            .name(name)
            .build();

        var result = testObject.invertConvert(domain);

        assertNotNull(result);
        assertEquals(uid, result.getUid());
        assertEquals(name, result.getName());
    }

    @ParameterizedTest
    @NullSource
    void invertConvert_shouldThrowException_whenArgumentNull(TrainingType trainingType) {
        var result = testObject.invertConvert(trainingType);

        assertNull(result);
    }


    private static Stream<Arguments> provideRoundtripEntityData() {
        return Stream.of(
            Arguments.of(UUID.randomUUID(), "CARDIO"),
            Arguments.of(UUID.randomUUID(), "YOGA")
        );
    }

    @ParameterizedTest
    @MethodSource("provideRoundtripEntityData")
    void roundtrip_shouldPreserveData_whenEntityToDomainToEntity(UUID uid, String name) {
        var originalEntity = TrainingTypeEntity.builder()
            .uid(uid)
            .name(name)
            .build();

        var domain = testObject.convert(originalEntity);
        assertNotNull(domain);
        var resultEntity = testObject.invertConvert(domain);

        assertEquals(originalEntity.getUid(), resultEntity.getUid());
        assertEquals(originalEntity.getName(), resultEntity.getName());
    }

    private static Stream<Arguments> provideRoundtripDomainData() {
        return Stream.of(
            Arguments.of(UUID.randomUUID(), "CARDIO"),
            Arguments.of(UUID.randomUUID(), "YOGA")
        );
    }

    @ParameterizedTest
    @MethodSource("provideRoundtripDomainData")
    void roundtrip_shouldPreserveData_whenDomainToEntityToDomain(UUID uid, String name) {
        var originalDomain = TrainingType.builder()
            .uid(uid)
            .name(name)
            .build();

        var entity = testObject.invertConvert(originalDomain);
        var resultDomain = testObject.convert(entity);

        assertEquals(originalDomain.getUid(), resultDomain.getUid());
        assertEquals(originalDomain.getName(), resultDomain.getName());
    }
}
