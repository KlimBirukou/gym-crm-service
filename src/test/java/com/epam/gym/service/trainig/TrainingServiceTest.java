package com.epam.gym.service.trainig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/*

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);
    private static final LocalDate FROM_DATE = LocalDate.of(2025, 12, 1);
    private static final LocalDate TO_DATE = LocalDate.of(2026, 2, 1);
    private static final UUID TRAINEE_UID = UUID.randomUUID();
    private static final UUID TRAINER_UID = UUID.randomUUID();
    private static final UUID TRAINING_TYPE_UID = UUID.randomUUID();
    private static final String TRAINEE_USERNAME = "trainee_username";
    private static final String TRAINER_USERNAME = "trainer_username";
    private static final String TRAINING_NAME = "training_name";
    private static final String TRAINING_TYPE_NAME = "training_type_name";
    private static final int DURATION_MINUTES = 60;

    @Mock
    private ITrainingRepository trainingRepository;
    @Mock
    private ITraineeService traineeService;
    @Mock
    private ITrainerService trainerService;
    @Mock
    private ITrainingTypeService trainingTypeService;
    @Mock
    private ITraineeAssignmentTrainerService traineeAssignmentTrainerService;

    @Captor
    private ArgumentCaptor<Training> trainingCaptor;

    private TrainingService testObject;

    @BeforeEach
    void setUp() {
        testObject = new TrainingService(
            trainingRepository,
            traineeService,
            trainerService,
            trainingTypeService,
            traineeAssignmentTrainerService
        );
    }

    @Test
    void create_shouldCreateTraining_whenValidationPassed() {
        var trainee = getTrainee(true);
        var trainer = getTrainer(true);
        var trainingTypeName = getTrainingType();
        var createTrainingDto = getCreateTrainingDto();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(trainer).when(trainerService).getByUsername(TRAINER_USERNAME);
        doNothing().when(traineeAssignmentTrainerService).checkAssignExist(TRAINEE_USERNAME, TRAINER_USERNAME);
        doReturn(trainingTypeName).when(trainingTypeService).getByName(TRAINING_TYPE_NAME);
        doReturn(List.of()).when(trainingRepository).getTrainingsOnDate(DATE);

        var result = testObject.create(createTrainingDto);

        verify(trainingRepository).save(trainingCaptor.capture());
        var saved = trainingCaptor.getValue();

        assertSame(result, saved);
        assertNotNull(saved.getUid());
        assertEquals(TRAINEE_UID, saved.getTraineeUid());
        assertEquals(TRAINER_UID, saved.getTrainerUid());
        assertEquals(TRAINING_NAME, saved.getName());
        assertEquals(trainingTypeName, saved.getTrainingType());
        assertEquals(DATE, saved.getDate());
        assertEquals(Duration.ofMinutes(DURATION_MINUTES), saved.getDuration());

        assertNoUnexpectedInteractions();
    }

    @Test
    void create_shouldThrowException_whenTraineeNotActive() {
        var trainee = getTrainee(false);
        var createTrainingDto = getCreateTrainingDto();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);

        assertThrows(TraineeNotActiveException.class, () -> testObject.create(createTrainingDto));

        assertNoUnexpectedInteractions();
    }

    @Test
    void create_shouldThrowException_whenTrainerNotActive() {
        var trainee = getTrainee(true);
        var trainer = getTrainer(false);
        var createTrainingDto = getCreateTrainingDto();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(trainer).when(trainerService).getByUsername(TRAINER_USERNAME);

        assertThrows(TrainerNotActiveException.class, () -> testObject.create(createTrainingDto));

        assertNoUnexpectedInteractions();
    }

    @Test
    void create_shouldThrowException_whenTrainingTypeMismatch() {
        var trainee = getTrainee(true);
        var trainingTypeName = getTrainingType();
        var createTrainingDto = getCreateTrainingDto();
        var wrongType = TrainingType.builder()
            .uid(UUID.randomUUID())
            .name("WRONG_TYPE")
            .build();
        var trainerWithWrongType = Trainer.builder()
            .uid(TRAINER_UID)
            .username(TRAINER_USERNAME)
            .specialization(wrongType)
            .active(true)
            .build();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(trainerWithWrongType).when(trainerService).getByUsername(TRAINER_USERNAME);
        doNothing().when(traineeAssignmentTrainerService).checkAssignExist(TRAINEE_USERNAME, TRAINER_USERNAME);
        doReturn(trainingTypeName).when(trainingTypeService).getByName(TRAINING_TYPE_NAME);

        assertThrows(TrainingTypeMismatchException.class, () -> testObject.create(createTrainingDto));

        assertNoUnexpectedInteractions();
    }

    @Test
    void create_shouldThrowException_whenTraineeDateConflict() {
        var trainee = getTrainee(true);
        var trainer = getTrainer(true);
        var trainingTypeName = getTrainingType();
        var createTrainingDto = getCreateTrainingDto();
        var existingTraining = Training.builder()
            .uid(UUID.randomUUID())
            .traineeUid(TRAINEE_UID)
            .trainerUid(UUID.randomUUID())
            .date(DATE)
            .build();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(trainer).when(trainerService).getByUsername(TRAINER_USERNAME);
        doNothing().when(traineeAssignmentTrainerService).checkAssignExist(TRAINEE_USERNAME, TRAINER_USERNAME);
        doReturn(trainingTypeName).when(trainingTypeService).getByName(TRAINING_TYPE_NAME);
        doReturn(List.of(existingTraining)).when(trainingRepository).getTrainingsOnDate(DATE);

        assertThrows(TrainingDateConflictException.class, () -> testObject.create(createTrainingDto));

        assertNoUnexpectedInteractions();
    }

    @Test
    void create_shouldThrowException_whenTrainerDateConflict() {
        var trainee = getTrainee(true);
        var trainer = getTrainer(true);
        var trainingTypeName = getTrainingType();
        var createTrainingDto = getCreateTrainingDto();
        var existingTraining = Training.builder()
            .uid(UUID.randomUUID())
            .traineeUid(UUID.randomUUID())
            .trainerUid(TRAINER_UID)
            .date(DATE)
            .build();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(trainer).when(trainerService).getByUsername(TRAINER_USERNAME);
        doNothing().when(traineeAssignmentTrainerService).checkAssignExist(TRAINEE_USERNAME, TRAINER_USERNAME);
        doReturn(trainingTypeName).when(trainingTypeService).getByName(TRAINING_TYPE_NAME);
        doReturn(List.of(existingTraining)).when(trainingRepository).getTrainingsOnDate(DATE);

        assertThrows(TrainingDateConflictException.class, () -> testObject.create(createTrainingDto));

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void create_shouldThrowException_whenArgumentNull(CreateTrainingDto dto) {
        assertThrows(NullPointerException.class, () -> testObject.create(dto));

        assertNoUnexpectedInteractions();
    }


    @Test
    void getTraineeTrainings_shouldReturnList_whenValidInput() {
        var trainee = getTrainee(true);
        var trainingTypeName = getTrainingType();
        var training = getTraining();
        var criteriaDto = getCriteriaDto(TRAINEE_USERNAME);
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(trainingTypeName).when(trainingTypeService).getByName(TRAINING_TYPE_NAME);
        doReturn(List.of(training)).when(trainingRepository).getTraineeTrainings(TRAINEE_UID, TRAINING_TYPE_UID);

        var result = testObject.getTraineeTrainings(criteriaDto);

        assertEquals(1, result.size());
        assertEquals(training, result.getFirst());

        assertNoUnexpectedInteractions();
    }

    @Test
    void getTraineeTrainings_shouldFilterByDateRange_checkAllBranches() {
        var trainee = getTrainee(true);
        var trainingTypeName = getTrainingType();
        var criteriaDto = getCriteriaDto(TRAINEE_USERNAME);
        var trainingBefore = Training.builder().date(FROM_DATE.minusDays(1)).build();
        var trainingOnStart = Training.builder().date(FROM_DATE).build();
        var trainingInside = Training.builder().date(DATE).build();
        var trainingOnEnd = Training.builder().date(TO_DATE).build();
        var trainingAfter = Training.builder().date(TO_DATE.plusDays(1)).build();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(trainingTypeName).when(trainingTypeService).getByName(TRAINING_TYPE_NAME);
        doReturn(List.of(trainingBefore, trainingOnStart, trainingInside, trainingOnEnd, trainingAfter))
            .when(trainingRepository).getTraineeTrainings(TRAINEE_UID, TRAINING_TYPE_UID);

        var result = testObject.getTraineeTrainings(criteriaDto);

        assertEquals(3, result.size());
        assertEquals(trainingOnStart, result.get(0));
        assertEquals(trainingInside, result.get(1));
        assertEquals(trainingOnEnd, result.get(2));

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void getTraineeTrainings_shouldThrowException_whenArgumentNull(TraineeTrainingsDto dto) {
        assertThrows(NullPointerException.class, () -> testObject.getTraineeTrainings(dto));

        assertNoUnexpectedInteractions();
    }


    @Test
    void getTrainerTrainings_shouldReturnList_whenValidInput() {
        var trainer = getTrainer(true);
        var trainingTypeName = getTrainingType();
        var training = getTraining();
        var criteriaDto = getCriteriaDto(TRAINER_USERNAME);
        doReturn(trainer).when(trainerService).getByUsername(TRAINER_USERNAME);
        doReturn(trainingTypeName).when(trainingTypeService).getByName(TRAINING_TYPE_NAME);
        doReturn(List.of(training)).when(trainingRepository).getTrainerTrainings(TRAINER_UID, TRAINING_TYPE_UID);

        var result = testObject.getTrainerTrainings(criteriaDto);

        assertEquals(1, result.size());
        assertEquals(training, result.getFirst());

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void getTrainerTrainings_shouldThrowException_whenArgumentNull(TraineeTrainingsDto dto) {
        assertThrows(NullPointerException.class, () -> testObject.getTrainerTrainings(dto));

        assertNoUnexpectedInteractions();
    }

    private static CreateTrainingDto getCreateTrainingDto() {
        return new CreateTrainingDto(
            TRAINEE_USERNAME,
            TRAINER_USERNAME,
            TRAINING_NAME,
            TRAINING_TYPE_NAME,
            DATE,
            DURATION_MINUTES
        );
    }

    private static TrainingType getTrainingType() {
        return TrainingType.builder()
            .uid(TRAINING_TYPE_UID)
            .name(TRAINING_TYPE_NAME)
            .build();
    }

    private static Trainee getTrainee(boolean status) {
        return Trainee.builder()
            .uid(TRAINEE_UID)
            .username(TRAINEE_USERNAME)
            .active(status)
            .build();
    }

    private static Trainer getTrainer(boolean status) {
        return Trainer.builder()
            .uid(TRAINER_UID)
            .username(TRAINER_USERNAME)
            .specialization(getTrainingType())
            .active(status)
            .build();
    }

    private static Training getTraining() {
        return Training.builder()
            .uid(UUID.randomUUID())
            .traineeUid(UUID.randomUUID())
            .trainerUid(UUID.randomUUID())
            .date(DATE)
            .build();
    }

    private static TraineeTrainingsDto getCriteriaDto(String username) {
        return new TraineeTrainingsDto(
            username, TRAINING_TYPE_NAME, FROM_DATE, TO_DATE
        );
    }

    private void assertNoUnexpectedInteractions() {
        verifyNoMoreInteractions(
            trainingRepository,
            traineeService,
            trainerService,
            trainingTypeService,
            traineeAssignmentTrainerService
        );
    }
}
*/
