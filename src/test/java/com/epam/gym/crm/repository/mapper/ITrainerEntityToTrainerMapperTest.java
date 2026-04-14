package com.epam.gym.crm.repository.mapper;

import com.epam.gym.crm.configuration.ConversionServiceAdapter;
import com.epam.gym.crm.domain.training.TrainingType;
import com.epam.gym.crm.domain.user.Trainer;
import com.epam.gym.crm.repository.entity.TrainerEntity;
import com.epam.gym.crm.repository.entity.TrainingTypeEntity;
import com.epam.gym.crm.repository.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
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

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
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

        var result = testObject.convert(trainer);

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
        var result = testObject.convert(trainer);

        assertNull(result);
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
