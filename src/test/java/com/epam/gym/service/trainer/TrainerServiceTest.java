package com.epam.gym.service.trainer;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.exception.DomainNotFoundException;
import com.epam.gym.mother.TrainerMother;
import com.epam.gym.mother.dto.trainer.CreateTrainerDtoMother;
import com.epam.gym.mother.dto.trainer.UpdateTrainerDtoMother;
import com.epam.gym.repository.trainer.ITrainerRepository;
import com.epam.gym.service.generator.name.IUsernameGenerator;
import com.epam.gym.service.generator.password.IPasswordGenerator;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    private static final UUID UID = UUID.randomUUID();
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String SPECIALIZATION = "specialization";
    private static final String NEW_SPECIALIZATION = "new specialization";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final CreateTrainerDto CREATE_TRAINER_DTO =
        CreateTrainerDtoMother.get(FIRSTNAME, LASTNAME, SPECIALIZATION);
    private static final UpdateTrainerDto UPDATE_TRAINER_DTO =
        UpdateTrainerDtoMother.get(UID, NEW_SPECIALIZATION);
    private static final Trainer EXISTED_TRAINER =
        TrainerMother.get(UID, FIRSTNAME, LASTNAME, USERNAME);

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

    @Test
    void create_shouldCreateTrainer_whenAlways() {
        doReturn(USERNAME).when(usernameGenerator)
            .generate(FIRSTNAME, LASTNAME);
        doReturn(PASSWORD).when(passwordGenerator)
            .generate();

        var result = testObject.create(CREATE_TRAINER_DTO);
        verify(trainerRepository).save(trainerCaptor.capture());
        var saved = trainerCaptor.getValue();

        assertNotNull(result);
        assertNotNull(result.getUid());
        assertEquals(CREATE_TRAINER_DTO.firstName(), saved.getFirstName());
        assertEquals(CREATE_TRAINER_DTO.lastName(), saved.getLastName());
        assertEquals(CREATE_TRAINER_DTO.specialization(), saved.getSpecialization());
        assertEquals(USERNAME, saved.getUsername());
        assertEquals(PASSWORD, saved.getPassword());
        assertTrue(saved.isActive());

        assertEquals(result.getUid(), saved.getUid());

        verify(usernameGenerator, times(1))
            .generate(FIRSTNAME, LASTNAME);
        verify(passwordGenerator, times(1))
            .generate();
        verify(trainerRepository, times(1))
            .save(saved);
        verifyNoMoreInteractions(usernameGenerator, passwordGenerator, trainerRepository);
    }

    @ParameterizedTest
    @NullSource
    void create_shouldThrowException_whenDataNull(CreateTrainerDto dto) {
        assertThrows(NullPointerException.class,
            () -> testObject.create(dto));
    }

    @Test
    void update_shouldUpdateTrainer_whenTrainerExist() {
        doReturn(Optional.of(EXISTED_TRAINER)).when(trainerRepository)
            .findByUid(UID);

        testObject.update(UPDATE_TRAINER_DTO);
        verify(trainerRepository).save(trainerCaptor.capture());
        var saved = trainerCaptor.getValue();

        assertEquals(UID, saved.getUid());
        assertEquals(FIRSTNAME, saved.getFirstName());
        assertEquals(LASTNAME, saved.getLastName());
        assertEquals(NEW_SPECIALIZATION, saved.getSpecialization());
        assertEquals(USERNAME, saved.getUsername());
        assertEquals(PASSWORD, saved.getPassword());
        assertTrue(saved.isActive());

        verify(trainerRepository, times(1))
            .findByUid(UID);
        verify(trainerRepository, times(1))
            .save(saved);
        verifyNoMoreInteractions(usernameGenerator, passwordGenerator, trainerRepository);
    }

    @Test
    void update_shouldThrowException_whenTrainerNotExist() {
        doReturn(Optional.empty()).when(trainerRepository)
            .findByUid(UID);

        assertThrows(DomainNotFoundException.class,
            () -> testObject.update(UPDATE_TRAINER_DTO));

        verify(trainerRepository, times(1))
            .findByUid(UID);
        verifyNoInteractions(usernameGenerator, passwordGenerator);
        verifyNoMoreInteractions(trainerRepository);
    }

    @ParameterizedTest
    @NullSource
    void update_shouldThrowException_whenDataNull(com.epam.gym.service.trainer.dto.UpdateTrainerDto dto) {
        assertThrows(NullPointerException.class,
            () -> testObject.update(dto));
    }

}
