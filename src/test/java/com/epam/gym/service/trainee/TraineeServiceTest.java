package com.epam.gym.service.trainee;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.v1.repository.user.trainee.ITraineeRepository;
import com.epam.gym.mother.TraineeMother;
import com.epam.gym.mother.dto.trainee.CreateTraineeDtoMother;
import com.epam.gym.mother.dto.trainee.UpdateTraineeDtoMother;
import com.epam.gym.service.generator.name.IUsernameGenerator;
import com.epam.gym.service.generator.password.IPasswordGenerator;
import com.epam.gym.v1.service.trainee.TraineeService;
import com.epam.gym.v1.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.v1.service.trainee.dto.UpdateTraineeDto;
import com.epam.gym.validator.IValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);
    private static final UUID UID = UUID.randomUUID();
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String ADDRESS = "address";
    private static final String NEW_ADDRESS = "new address";
    private static final CreateTraineeDto CREATE_TRAINEE_DTO =
        CreateTraineeDtoMother.get(FIRSTNAME, LASTNAME, ADDRESS, DATE);
    private static final UpdateTraineeDto UPDATE_TRAINEE_DTO =
        UpdateTraineeDtoMother.get(UID, NEW_ADDRESS);
    private static final Trainee EXISTED_TRAINEE =
        TraineeMother.get(UID, FIRSTNAME, LASTNAME, USERNAME);

    @Mock
    private IUsernameGenerator usernameGenerator;
    @Mock
    private IPasswordGenerator passwordGenerator;
    @Mock
    private ITraineeRepository traineeRepository;
    @Mock
    private IValidator<CreateTraineeDto> createTraineeValidator;
    @Mock
    private IValidator<UUID> deleteTraineeValidator;

    @Captor
    private ArgumentCaptor<Trainee> traineeCaptor;

    private TraineeService testObject;

    @BeforeEach
    void setUp() {
        testObject = new TraineeService(
            usernameGenerator,
            passwordGenerator,
            traineeRepository,
            createTraineeValidator,
            deleteTraineeValidator
        );
    }

    @Test
    void create_shouldCreateTrainee_whenValidationPassed() {
        doNothing().when(createTraineeValidator)
            .validate(CREATE_TRAINEE_DTO);
        doReturn(USERNAME).when(usernameGenerator)
            .generate(FIRSTNAME, LASTNAME);
        doReturn(PASSWORD).when(passwordGenerator)
            .generate();

        var result = testObject.create(CREATE_TRAINEE_DTO);
        verify(traineeRepository).save(traineeCaptor.capture());
        var saved = traineeCaptor.getValue();

        assertNotNull(result);
        assertNotNull(result.getUid());
        assertEquals(CREATE_TRAINEE_DTO.firstName(), saved.getFirstName());
        assertEquals(CREATE_TRAINEE_DTO.lastName(), saved.getLastName());
        assertEquals(CREATE_TRAINEE_DTO.address(), saved.getAddress());
        assertEquals(CREATE_TRAINEE_DTO.birthdate(), saved.getBirthdate());
        assertEquals(USERNAME, saved.getUsername());
        assertEquals(PASSWORD, saved.getPassword());
        assertTrue(saved.isActive());

        assertEquals(result.getUid(), saved.getUid());

        verify(createTraineeValidator, times(1))
            .validate(CREATE_TRAINEE_DTO);
        verify(usernameGenerator, times(1))
            .generate(FIRSTNAME, LASTNAME);
        verify(passwordGenerator, times(1))
            .generate();
        verify(traineeRepository, times(1))
            .save(saved);
        verifyNoInteractions(deleteTraineeValidator);
        verifyNoMoreInteractions(createTraineeValidator, usernameGenerator, passwordGenerator, traineeRepository);
    }

    @Test
    void create_shouldThrowException_whenValidationFailed() {
        doThrow(new RuntimeException()).when(createTraineeValidator)
            .validate(CREATE_TRAINEE_DTO);

        assertThrows(RuntimeException.class,
            () -> testObject.create(CREATE_TRAINEE_DTO));

        verify(createTraineeValidator, times(1))
            .validate(CREATE_TRAINEE_DTO);
        verifyNoInteractions(usernameGenerator, passwordGenerator, traineeRepository, deleteTraineeValidator);
        verifyNoMoreInteractions(createTraineeValidator);
    }

    @ParameterizedTest
    @NullSource
    void create_shouldThrowException_whenDataNull(CreateTraineeDto dto) {
        assertThrows(NullPointerException.class,
            () -> testObject.create(dto));
    }

    @Test
    void update_shouldUpdateTrainee_whenTraineeExist() {
        doReturn(Optional.of(EXISTED_TRAINEE)).when(traineeRepository)
            .findByUid(UID);

        testObject.update(UPDATE_TRAINEE_DTO);
        verify(traineeRepository).save(traineeCaptor.capture());
        var saved = traineeCaptor.getValue();

        assertEquals(UID, saved.getUid());
        assertEquals(FIRSTNAME, saved.getFirstName());
        assertEquals(LASTNAME, saved.getLastName());
        assertEquals(NEW_ADDRESS, saved.getAddress());
        assertEquals(DATE, saved.getBirthdate());
        assertEquals(USERNAME, saved.getUsername());
        assertEquals(PASSWORD, saved.getPassword());
        assertTrue(saved.isActive());

        verify(traineeRepository, times(1))
            .findByUid(UID);
        verify(traineeRepository, times(1))
            .save(saved);
        verifyNoInteractions(createTraineeValidator, usernameGenerator, passwordGenerator, deleteTraineeValidator);
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    void update_shouldThrowException_whenTraineeNotExist() {
        doReturn(Optional.empty()).when(traineeRepository)
            .findByUid(UID);

        assertThrows(DomainNotFoundException.class,
            () -> testObject.update(UPDATE_TRAINEE_DTO));

        verify(traineeRepository, times(1))
            .findByUid(UID);
        verifyNoInteractions(createTraineeValidator, usernameGenerator, passwordGenerator, deleteTraineeValidator);
        verifyNoMoreInteractions(traineeRepository);
    }

    @ParameterizedTest
    @NullSource
    void update_shouldThrowException_whenDataNull(UpdateTraineeDto dto) {
        assertThrows(NullPointerException.class,
            () -> testObject.update(dto));
    }

    @Test
    void delete_shouldDeleteTrainee_whenValidationPassed() {
        doNothing().when(deleteTraineeValidator)
            .validate(UID);

        testObject.delete(UID);

        verify(deleteTraineeValidator, times(1))
            .validate(UID);
        verify(traineeRepository, times(1))
            .deleteByUid(UID);
        verifyNoInteractions(createTraineeValidator, usernameGenerator, passwordGenerator);
        verifyNoMoreInteractions(deleteTraineeValidator, traineeRepository);
    }

    @Test
    void delete_shouldThrowException_whenValidationFailed() {
        doThrow(new RuntimeException()).when(deleteTraineeValidator)
            .validate(UID);

        assertThrows(RuntimeException.class,
            () -> testObject.delete(UID));

        verify(deleteTraineeValidator, times(1))
            .validate(UID);
        verifyNoInteractions(createTraineeValidator, usernameGenerator, passwordGenerator, traineeRepository);
        verifyNoMoreInteractions(deleteTraineeValidator);
    }

    @ParameterizedTest
    @NullSource
    void delete_shouldThrowException_whenUidNull(UUID uid) {
        assertThrows(NullPointerException.class,
            () -> testObject.delete(uid));
    }
}
