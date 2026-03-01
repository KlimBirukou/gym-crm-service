package com.epam.gym.service.trainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/*
@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    private static final UUID UID = UUID.randomUUID();
    private static final String FIRSTNAME = "firstname";
    private static final String NEW_FIRSTNAME = "new_firstname";
    private static final String LASTNAME = "lastname";
    private static final String NEW_LASTNAME = "new_lastname";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String HASHED_PASSWORD = "hashed_password";
    private static final String NEW_PASSWORD = "new_password";
    private static final String NEW_HASHED_PASSWORD = "new_hashed_password";
    private static final String SPECIALIZATION_NAME = "specialization_name";

    @Mock
    private IUsernameGenerator usernameGenerator;
    @Mock
    private IPasswordGenerator passwordGenerator;
    @Mock
    private ITrainerRepository trainerRepository;
    @Mock
    private ITrainingTypeService trainingTypeService;
    @Mock
    private IPasswordService passwordService;

    @Captor
    private ArgumentCaptor<Trainer> trainerCaptor;

    private TrainerService testObject;

    @BeforeEach
    void setUp() {
        testObject = new TrainerService(
            passwordGenerator,
            usernameGenerator,
            trainerRepository,
            trainingTypeService,
            passwordService
        );
    }

    @Test
    void create_shouldCreateTrainer_whenAlways() {
        var trainingType = getTrainingType();
        var createTrainerDto = getCreateTrainerDto();
        doReturn(USERNAME).when(usernameGenerator).generate(FIRSTNAME, LASTNAME);
        doReturn(PASSWORD).when(passwordGenerator).generate();
        doReturn(trainingType).when(trainingTypeService).getByName(SPECIALIZATION_NAME);
        doReturn(HASHED_PASSWORD).when(passwordService).hashPassword(PASSWORD);

        var result = testObject.create(createTrainerDto);

        verify(trainerRepository).save(trainerCaptor.capture());
        var saved = trainerCaptor.getValue();

        assertSame(result, saved);
        assertNotNull(saved.getUid());
        assertEquals(FIRSTNAME, saved.getFirstName());
        assertEquals(LASTNAME, saved.getLastName());
        assertEquals(trainingType, saved.getSpecialization());
        assertEquals(USERNAME, saved.getUsername());
        assertEquals(HASHED_PASSWORD, saved.getPassword());
        assertTrue(saved.isActive());

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void create_shouldThrowException_whenArgumentNull(CreateTrainerDto dto) {
        assertThrows(NullPointerException.class, () -> testObject.create(dto));

        assertNoUnexpectedInteractions();
    }


    @Test
    void update_shouldUpdateTrainer_whenTrainerExist() {
        var trainer = getTrainer();
        var trainerType = getTrainingType();
        var updateTrainerDto = getUpdateTrainerDto();
        doReturn(Optional.of(trainer)).when(trainerRepository).getByUsername(USERNAME);

        testObject.update(updateTrainerDto);

        verify(trainerRepository).save(trainerCaptor.capture());
        var saved = trainerCaptor.getValue();

        assertEquals(NEW_FIRSTNAME, saved.getFirstName());
        assertEquals(NEW_LASTNAME, saved.getLastName());
        assertEquals(trainerType, saved.getSpecialization());
        assertEquals(UID, saved.getUid());
        assertEquals(USERNAME, saved.getUsername());
        assertEquals(HASHED_PASSWORD, saved.getPassword());
        assertTrue(saved.isActive());

        assertNoUnexpectedInteractions();
    }

    @Test
    void update_shouldThrowException_whenTrainerNotExist() {
        var updateTrainerDto = getUpdateTrainerDto();
        doReturn(Optional.empty()).when(trainerRepository).getByUsername(USERNAME);

        assertThrows(TrainerNotFoundException.class, () -> testObject.update(updateTrainerDto));

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void update_shouldThrowException_whenDataNull(UpdateTrainerDto dto) {
        assertThrows(NullPointerException.class, () -> testObject.update(dto));

        assertNoUnexpectedInteractions();
    }


    @Test
    void changePassword_shouldUpdatePassword_whenOldPasswordMatch() {
        var trainee = getTrainer();
        var changePasswordDto = getChangePasswordDto();
        doReturn(Optional.of(trainee)).when(trainerRepository).getByUsername(USERNAME);
        doReturn(true).when(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);
        doReturn(NEW_HASHED_PASSWORD).when(passwordService).hashPassword(NEW_PASSWORD);

        testObject.changePassword(changePasswordDto);

        verify(trainerRepository).save(trainerCaptor.capture());
        var saved = trainerCaptor.getValue();

        assertEquals(NEW_HASHED_PASSWORD, saved.getPassword());
        assertEquals(UID, saved.getUid());
        assertEquals(USERNAME, saved.getUsername());
        assertTrue(saved.isActive());

        assertNoUnexpectedInteractions();
    }

    @Test
    void changePassword_shouldThrowException_whenOldPasswordNotMatch() {
        var trainee = getTrainer();
        var changePasswordDto = getChangePasswordDto();
        doReturn(Optional.of(trainee)).when(trainerRepository).getByUsername(USERNAME);
        doReturn(false).when(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);

        assertThrows(AuthException.class, () -> testObject.changePassword(changePasswordDto));

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void changePassword_shouldThrowException_whenArgumentNull(ChangePasswordDto dto) {
        assertThrows(NullPointerException.class, () -> testObject.changePassword(dto));

        assertNoUnexpectedInteractions();
    }


    @Test
    void toggleStatus_shouldDeactivate_whenTrainerIsActive() {
        var trainee = getTrainer();
        doReturn(Optional.of(trainee)).when(trainerRepository).getByUsername(USERNAME);

        testObject.toggleStatus(USERNAME);

        verify(trainerRepository).save(trainerCaptor.capture());
        assertFalse(trainerCaptor.getValue().isActive());

        assertNoUnexpectedInteractions();
    }

    @Test
    void toggleStatus_shouldActivate_whenTrainerIsInactive() {
        var inactiveTrainer = Trainer.builder()
            .uid(UID)
            .username(USERNAME)
            .active(false)
            .build();
        doReturn(Optional.of(inactiveTrainer)).when(trainerRepository).getByUsername(USERNAME);

        testObject.toggleStatus(USERNAME);

        verify(trainerRepository).save(trainerCaptor.capture());
        assertTrue(trainerCaptor.getValue().isActive());

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void toggleStatus_shouldThrowNullPointerException_whenArgumentNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.toggleStatus(username));

        assertNoUnexpectedInteractions();
    }


    @Test
    void getByUsername_shouldReturnTrainer_whenTrainerExists() {
        var trainee = getTrainer();
        doReturn(Optional.of(trainee)).when(trainerRepository).getByUsername(USERNAME);

        var result = testObject.getByUsername(USERNAME);

        assertSame(trainee, result);
        assertNoUnexpectedInteractions();
    }

    @Test
    void getByUsername_shouldThrowException_whenTrainerNotExists() {
        doReturn(Optional.empty()).when(trainerRepository).getByUsername(USERNAME);

        assertThrows(TrainerNotFoundException.class, () -> testObject.getByUsername(USERNAME));

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void getByUsername_shouldThrowException_whenArgumentNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.getByUsername(username));

        assertNoUnexpectedInteractions();
    }

    private static CreateTrainerDto getCreateTrainerDto() {
        return new CreateTrainerDto(
            FIRSTNAME, LASTNAME, SPECIALIZATION_NAME
        );
    }

    private static UpdateTrainerDto getUpdateTrainerDto() {
        return new UpdateTrainerDto(
            USERNAME, NEW_FIRSTNAME, NEW_LASTNAME
        );
    }

    private static ChangePasswordDto getChangePasswordDto() {
        return new ChangePasswordDto(
            USERNAME, PASSWORD, NEW_PASSWORD
        );
    }

    private static TrainingType getTrainingType() {
        return TrainingType.builder()
            .uid(UID)
            .name(SPECIALIZATION_NAME)
            .build();
    }

    private static Trainer getTrainer() {
        return Trainer.builder()
            .uid(UID)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .username(USERNAME)
            .password(HASHED_PASSWORD)
            .specialization(getTrainingType())
            .active(true)
            .build();
    }

    private void assertNoUnexpectedInteractions() {
        verifyNoMoreInteractions(
            passwordGenerator,
            usernameGenerator,
            trainerRepository,
            trainingTypeService,
            passwordService
        );
    }
}
*/
