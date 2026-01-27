package com.epam.gym.service.trainer;

import com.epam.gym.GymApplication;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.trainer.ITrainerRepository;
import com.epam.gym.service.generator.name.IUsernameGenerator;
import com.epam.gym.service.generator.password.IPasswordGenerator;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {

    private static final UUID UID = UUID.randomUUID();
    private static final String FIRSTNAME = "John";
    private static final String LASTNAME = "Doe";
    private static final String SPECIALIZATION = "Strength training";
    private static final String NEW_SPECIALIZATION = "Yoga";
    private static final String PASSWORD = "7a3d9f2b6c";
    private static final String USERNAME = String.join(GymApplication.DEFAULT_USERNAME_DELIMITER,
        FIRSTNAME,
        LASTNAME);
    private static final String EX_MESSAGE_TRAINER_NOT_FOUND = "Trainer not found";
    private static final CreateTrainerDto CREATE_TRAINER_DTO = new CreateTrainerDto(FIRSTNAME, LASTNAME, SPECIALIZATION);
    private static final UpdateTrainerDto UPDATE_TRAINER_DTO = new UpdateTrainerDto(UID, NEW_SPECIALIZATION);


    @Mock
    private IUsernameGenerator usernameGenerator;
    @Mock
    private IPasswordGenerator passwordGenerator;
    @Mock
    private ITrainerRepository trainerRepository;

    @Captor
    private ArgumentCaptor<Trainer> trainerCaptor;

    @InjectMocks
    private TrainerService testObject;

    @BeforeEach
    void setUp() {
        testObject.setUsernameGenerator(usernameGenerator);
        testObject.setPasswordGenerator(passwordGenerator);
        testObject.setTrainerRepository(trainerRepository);
    }

    @Test
    void create_shouldBuildSaveAndReturnTrainer() {
        when(usernameGenerator.generate(FIRSTNAME, LASTNAME))
            .thenReturn(USERNAME);
        when(passwordGenerator.generate())
            .thenReturn(PASSWORD);

        var result = testObject.create(CREATE_TRAINER_DTO);

        assertNotNull(result);
        assertNotNull(result.getUid());
        assertEquals(FIRSTNAME, result.getFirstName());
        assertEquals(LASTNAME, result.getLastName());
        assertEquals(SPECIALIZATION, result.getSpecialization());
        assertEquals(USERNAME, result.getUsername());
        assertEquals(PASSWORD, result.getPassword());
        assertTrue(result.isActive());

        verify(usernameGenerator, times(1)).generate(FIRSTNAME, LASTNAME);
        verify(passwordGenerator, times(1)).generate();
        verify(trainerRepository, times(1)).save(trainerCaptor.capture());
        assertEquals(result, trainerCaptor.getValue());
    }

    @Test
    void create_shouldThrowNpe_whenDtoIsNull() {
        assertThrows(NullPointerException.class,
            () -> testObject.create(null));

        verify(usernameGenerator, never()).generate(any(), any());
        verify(passwordGenerator, never()).generate();
        verify(trainerRepository, never()).save(any());
    }

    @Test
    void update_shouldUpdateExistingTrainee() {
        var existingTrainer = buildTrainer(UID);
        when(trainerRepository.findByUid(UID))
            .thenReturn(Optional.of(existingTrainer));

        testObject.update(UPDATE_TRAINER_DTO);

        verify(trainerRepository).findByUid(UID);
        verify(trainerRepository).save(trainerCaptor.capture());

        var updatedTrainer = trainerCaptor.getValue();
        assertEquals(existingTrainer.getUid(), updatedTrainer.getUid());
        assertEquals(UPDATE_TRAINER_DTO.specialization(), updatedTrainer.getSpecialization());
        assertEquals(existingTrainer.getFirstName(), updatedTrainer.getFirstName());
        assertEquals(existingTrainer.getLastName(), updatedTrainer.getLastName());
        assertEquals(existingTrainer.getUsername(), updatedTrainer.getUsername());
        assertEquals(existingTrainer.getPassword(), updatedTrainer.getPassword());
    }

    @Test
    void update_shouldThrownException_whenTraineeNotFound() {
        when(trainerRepository.findByUid(UID))
            .thenReturn(Optional.empty());

        var exception = assertThrows(RuntimeException.class,
            () -> testObject.update(UPDATE_TRAINER_DTO));

        assertEquals(EX_MESSAGE_TRAINER_NOT_FOUND, exception.getMessage());
        verify(trainerRepository, times(1)).findByUid(UID);
        verify(trainerRepository, never()).save(any());
    }

    @Test
    void update_shouldThrowNpe_whenDtoIsNull() {
        assertThrows(NullPointerException.class,
            () -> testObject.update(null));

        verify(trainerRepository, never()).findByUid(any());
        verify(trainerRepository, never()).save(any());
    }

    private Trainer buildTrainer(UUID uid) {
        return Trainer.builder()
            .uid(uid)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .specialization(SPECIALIZATION)
            .username(USERNAME)
            .password(PASSWORD)
            .isActive(true)
            .build();
    }

}
