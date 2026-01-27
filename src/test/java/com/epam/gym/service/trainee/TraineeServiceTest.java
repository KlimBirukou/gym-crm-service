package com.epam.gym.service.trainee;

import com.epam.gym.GymApplication;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.repository.trainee.ITraineeRepository;
import com.epam.gym.service.generator.name.IUsernameGenerator;
import com.epam.gym.service.generator.password.IPasswordGenerator;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainee.dto.UpdateTraineeDto;
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
public class TraineeServiceTest {

    private static final UUID UID = UUID.randomUUID();
    private static final String FIRSTNAME = "John";
    private static final String LASTNAME = "Doe";
    private static final String ADDRESS = "Main Street 21";
    private static final String NEW_ADDRESS = "New Main Street 21";
    private static final String PASSWORD = "7a3d9f2b6c";
    private static final String USERNAME = String.join(GymApplication.DEFAULT_USERNAME_DELIMITER,
        FIRSTNAME,
        LASTNAME);
    private static final String EX_MESSAGE_TRAINEE_NOT_FOUND = "Trainee not found";
    private static final CreateTraineeDto CREATE_TRAINEE_DTO = new CreateTraineeDto(FIRSTNAME, LASTNAME, ADDRESS);
    private static final UpdateTraineeDto UPDATE_TRAINEE_DTO = new UpdateTraineeDto(UID, NEW_ADDRESS);

    @Mock
    private IUsernameGenerator usernameGenerator;
    @Mock
    private IPasswordGenerator passwordGenerator;
    @Mock
    private ITraineeRepository traineeRepository;

    @Captor
    private ArgumentCaptor<Trainee> traineeCaptor;

    @InjectMocks
    private TraineeService testObject;

    @BeforeEach
    void setUp() {
        testObject.setUsernameGenerator(usernameGenerator);
        testObject.setPasswordGenerator(passwordGenerator);
        testObject.setTrainerRepository(traineeRepository);
    }

    @Test
    void create_shouldBuildSaveAndReturnTrainee() {
        when(usernameGenerator.generate(FIRSTNAME, LASTNAME))
            .thenReturn(USERNAME);
        when(passwordGenerator.generate())
            .thenReturn(PASSWORD);

        var result = testObject.create(CREATE_TRAINEE_DTO);

        assertNotNull(result);
        assertNotNull(result.getUid());
        assertEquals(FIRSTNAME, result.getFirstName());
        assertEquals(LASTNAME, result.getLastName());
        assertEquals(ADDRESS, result.getAddress());
        assertEquals(USERNAME, result.getUsername());
        assertEquals(PASSWORD, result.getPassword());
        assertTrue(result.isActive());

        verify(usernameGenerator, times(1)).generate(FIRSTNAME, LASTNAME);
        verify(passwordGenerator, times(1)).generate();
        verify(traineeRepository, times(1)).save(traineeCaptor.capture());
        assertEquals(result, traineeCaptor.getValue());
    }

    @Test
    void create_shouldThrowNpe_whenDtoIsNull() {
        assertThrows(NullPointerException.class,
            () -> testObject.create(null));

        verify(usernameGenerator, never()).generate(any(), any());
        verify(passwordGenerator, never()).generate();
        verify(traineeRepository, never()).save(any());
    }

    @Test
    void update_shouldUpdateExistingTrainee() {
        var existingTrainee = buildTrainee(UID);
        when(traineeRepository.findByUid(UID))
            .thenReturn(Optional.of(existingTrainee));

        testObject.update(UPDATE_TRAINEE_DTO);

        verify(traineeRepository).findByUid(UID);
        verify(traineeRepository).save(traineeCaptor.capture());

        var updatedTrainee = traineeCaptor.getValue();
        assertEquals(existingTrainee.getUid(), updatedTrainee.getUid());
        assertEquals(UPDATE_TRAINEE_DTO.address(), updatedTrainee.getAddress());
        assertEquals(existingTrainee.getFirstName(), updatedTrainee.getFirstName());
        assertEquals(existingTrainee.getLastName(), updatedTrainee.getLastName());
        assertEquals(existingTrainee.getUsername(), updatedTrainee.getUsername());
        assertEquals(existingTrainee.getPassword(), updatedTrainee.getPassword());
    }

    @Test
    void update_shouldThrownException_whenTraineeNotFound() {
        when(traineeRepository.findByUid(UID))
            .thenReturn(Optional.empty());

        var exception = assertThrows(RuntimeException.class,
            () -> testObject.update(UPDATE_TRAINEE_DTO));

        assertEquals(EX_MESSAGE_TRAINEE_NOT_FOUND, exception.getMessage());
        verify(traineeRepository, times(1)).findByUid(UID);
        verify(traineeRepository, never()).save(any());
    }

    @Test
    void update_shouldThrowNpe_whenDtoIsNull() {
        assertThrows(NullPointerException.class,
            () -> testObject.update(null));

        verify(traineeRepository, never()).findByUid(any());
        verify(traineeRepository, never()).save(any());
    }

    @Test
    void delete_shouldDeleteExistingTrainee() {
        var existingTrainee = buildTrainee(UID);
        when(traineeRepository.findByUid(UID))
            .thenReturn(Optional.of(existingTrainee));

        testObject.delete(UID);

        verify(traineeRepository, times(1)).findByUid(UID);
        verify(traineeRepository, times(1)).deleteByUid(UID);
    }

    @Test
    void delete_shouldThrowException_whenTraineeNotFound() {
        when(traineeRepository.findByUid(UID))
            .thenReturn(Optional.empty());

        var exception = assertThrows(RuntimeException.class,
            () -> testObject.delete(UID));

        assertEquals(EX_MESSAGE_TRAINEE_NOT_FOUND, exception.getMessage());
        verify(traineeRepository, times(1)).findByUid(UID);
        verify(traineeRepository, never()).deleteByUid(any());
    }

    @Test
    void delete_shouldThrowNpe_whenUidIsNull() {
        assertThrows(NullPointerException.class,
            () -> testObject.delete(null));

        verify(traineeRepository, never()).findByUid(any());
        verify(traineeRepository, never()).deleteByUid(any());
    }

    private Trainee buildTrainee(UUID uid) {
        return Trainee.builder()
            .uid(uid)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .address(ADDRESS)
            .username(USERNAME)
            .password(PASSWORD)
            .isActive(true)
            .build();
    }
}
