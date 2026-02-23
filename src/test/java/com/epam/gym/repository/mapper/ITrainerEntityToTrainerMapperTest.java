package com.epam.gym.repository.mapper;

import com.epam.gym.configuration.ConversionServiceAdapter;
import com.epam.gym.domain.training.TrainingType;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.entity.TrainerEntity;
import com.epam.gym.repository.entity.TrainingTypeEntity;
import com.epam.gym.repository.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ITrainerEntityToTrainerMapperTest {

    private static final UUID TRAINER_UID = UUID.randomUUID();
    private static final UUID TYPE_UID = UUID.randomUUID();
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String SPECIALIZATION_NAME = "specialization_name";

    @Mock
    private ConversionServiceAdapter conversionServiceAdapter;

    private ITrainerEntityToTrainerMapper testObject;

    @BeforeEach
    void setUp() {
        testObject = new ITrainerEntityToTrainerMapperImpl(conversionServiceAdapter);
    }

    private static Stream<Boolean> provideConvertData() {
        return Stream.of(true, false);
    }

    @ParameterizedTest
    @MethodSource("provideConvertData")
    void convert_shouldMapEntityToDomain(boolean active) {
        var entity = createEntity(active);
        when(conversionServiceAdapter.mapTrainingTypeEntityToTrainingType(entity.getSpecialization()))
            .thenReturn(TrainingType.builder()
                .uid(TYPE_UID)
                .name(SPECIALIZATION_NAME)
                .build());

        var result = testObject.convert(entity);

        assertNotNull(result);
        assertEquals(TRAINER_UID, result.getUid());
        assertEquals(FIRSTNAME, result.getFirstName());
        assertEquals(SPECIALIZATION_NAME, result.getSpecialization().getName());
        assertEquals(active, result.isActive());
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenArgumentNull(TrainerEntity entity) {
        var result = testObject.convert(entity);

        assertNull(result);
    }


    private static Stream<Arguments> provideInvertConvertData() {
        return Stream.of(
            Arguments.of(true),
            Arguments.of(false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvertConvertData")
    void invertConvert_shouldMapDomainToEntity(boolean active) {
        var specialization = TrainingType.builder()
            .uid(TYPE_UID)
            .name(SPECIALIZATION_NAME)
            .build();
        var trainer = Trainer.builder()
            .uid(TRAINER_UID)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .username(USERNAME)
            .password(PASSWORD)
            .active(active)
            .specialization(specialization)
            .build();

        var result = testObject.invertConvert(trainer);

        assertNotNull(result);
        assertEquals(TRAINER_UID, result.getUid());
        assertNotNull(result.getUser());
        assertNull(result.getUser().getUid());
        assertEquals(FIRSTNAME, result.getUser().getFirstName());
        assertEquals(LASTNAME, result.getUser().getLastName());
        assertEquals(USERNAME, result.getUser().getUsername());
        assertEquals(PASSWORD, result.getUser().getPassword());
        assertEquals(active, result.getUser().isActive());
        assertNotNull(result.getSpecialization());
        assertEquals(TYPE_UID, result.getSpecialization().getUid());
        assertEquals(SPECIALIZATION_NAME, result.getSpecialization().getName());
    }

    @ParameterizedTest
    @NullSource
    void invertConvert_shouldReturnNull_whenArgumentNull(Trainer trainer) {
        var result = testObject.invertConvert(trainer);

        assertNull(result);
    }


    private static Stream<Arguments> provideRoundtripEntityData() {
        return Stream.of(
            Arguments.of(true),
            Arguments.of(false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideRoundtripEntityData")
    void roundtrip_shouldPreserveData_whenEntityToDomainToEntity(boolean active) {
        var originalEntity = createEntity(active);
        when(conversionServiceAdapter.mapTrainingTypeEntityToTrainingType(originalEntity.getSpecialization()))
            .thenReturn(TrainingType.builder()
                .uid(TYPE_UID)
                .name(SPECIALIZATION_NAME)
                .build());

        var domain = testObject.convert(originalEntity);
        assertNotNull(domain);
        var resultEntity = testObject.invertConvert(domain);

        assertNotNull(resultEntity);
        assertEquals(originalEntity.getUid(), resultEntity.getUid());

        assertNotNull(resultEntity.getUser());
        assertEquals(originalEntity.getUser().getFirstName(), resultEntity.getUser().getFirstName());
        assertEquals(originalEntity.getUser().getLastName(), resultEntity.getUser().getLastName());
        assertEquals(originalEntity.getUser().getUsername(), resultEntity.getUser().getUsername());
        assertEquals(originalEntity.getUser().getPassword(), resultEntity.getUser().getPassword());
        assertEquals(originalEntity.getUser().isActive(), resultEntity.getUser().isActive());

        assertNotNull(resultEntity.getSpecialization());
        assertEquals(originalEntity.getSpecialization().getUid(), resultEntity.getSpecialization().getUid());
        assertEquals(originalEntity.getSpecialization().getName(), resultEntity.getSpecialization().getName());
    }

    private static Stream<Arguments> provideRoundtripDomainData() {
        return Stream.of(
            Arguments.of(true),
            Arguments.of(false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideRoundtripDomainData")
    void roundtrip_shouldPreserveData_whenDomainToEntityToDomain(boolean active) {
        var originalDomain = Trainer.builder()
            .uid(TRAINER_UID)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .username(USERNAME)
            .password(PASSWORD)
            .active(active)
            .specialization(TrainingType.builder().uid(TYPE_UID).name(SPECIALIZATION_NAME).build())
            .build();
        when(conversionServiceAdapter.mapTrainingTypeEntityToTrainingType(any()))
            .thenReturn(originalDomain.getSpecialization());

        var entity = testObject.invertConvert(originalDomain);
        var resultDomain = testObject.convert(entity);

        assertNotNull(resultDomain);
        assertEquals(originalDomain.getUid(), resultDomain.getUid());
        assertEquals(originalDomain.getFirstName(), resultDomain.getFirstName());
        assertEquals(originalDomain.getLastName(), resultDomain.getLastName());
        assertEquals(originalDomain.getUsername(), resultDomain.getUsername());
        assertEquals(originalDomain.getPassword(), resultDomain.getPassword());
        assertEquals(originalDomain.isActive(), resultDomain.isActive());
        assertEquals(originalDomain.getSpecialization().getUid(), resultDomain.getSpecialization().getUid());
        assertEquals(originalDomain.getSpecialization().getName(), resultDomain.getSpecialization().getName());
    }

    private static TrainerEntity createEntity(boolean active) {
        return TrainerEntity.builder()
            .uid(TRAINER_UID)
            .user(UserEntity.builder()
                .firstName(FIRSTNAME)
                .lastName(LASTNAME)
                .username(USERNAME)
                .password(PASSWORD)
                .active(active)
                .build())
            .specialization(TrainingTypeEntity.builder()
                .uid(TYPE_UID)
                .name(SPECIALIZATION_NAME)
                .build())
            .build();
    }
}
