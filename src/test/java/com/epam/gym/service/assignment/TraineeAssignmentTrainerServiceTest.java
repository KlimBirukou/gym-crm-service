package com.epam.gym.service.assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/*@ExtendWith(MockitoExtension.class)
class TraineeAssignmentTrainerServiceTest {

    private static final String TRAINEE_USERNAME = "trainee_username";
    private static final String TRAINER_USERNAME = "trainer_username";
    private static final Trainer TRAINER_1 = new Trainer();
    private static final Trainer TRAINER_2 = new Trainer();

    @Mock
    private ITraineeService traineeService;
    @Mock
    private ITrainerService trainerService;
    @Mock
    private ITraineeAssignmentTrainerRepository traineeAssignmentTrainerRepository;

    private TraineeAssignmentTrainerService testObject;

    @BeforeEach
    void setUp() {
        testObject = new TraineeAssignmentTrainerService(
            traineeService,
            trainerService,
            traineeAssignmentTrainerRepository
        );
    }

    @Test
    void assign_shouldAssign_whenTraineeAndTrainerExistAndActive() {
        var trainee = Trainee.builder()
            .username(TRAINEE_USERNAME)
            .active(true)
            .build();
        var trainer = Trainer.builder()
            .username(TRAINER_USERNAME)
            .active(true)
            .build();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(trainer).when(trainerService).getByUsername(TRAINER_USERNAME);
        doReturn(true).when(traineeAssignmentTrainerRepository).checkAssign(TRAINEE_USERNAME, TRAINER_USERNAME);

        testObject.assign(TRAINEE_USERNAME, TRAINER_USERNAME);

        verify(traineeAssignmentTrainerRepository).assign(trainee, trainer);

        assertNoUnexpectedInteractions();
    }

    @Test
    void assign_shouldThrowException_whenTraineeNotActive() {
        var unactiveTrainee = Trainee.builder()
            .username(TRAINEE_USERNAME)
            .active(false)
            .build();
        doReturn(unactiveTrainee).when(traineeService).getByUsername(TRAINEE_USERNAME);

        assertThrows(TraineeNotActiveException.class, () -> testObject.assign(TRAINEE_USERNAME, TRAINER_USERNAME));

        assertNoUnexpectedInteractions();
    }

    @Test
    void assign_shouldThrowException_whenTrainerNotActive() {
        var trainee = Trainee.builder()
            .username(TRAINEE_USERNAME)
            .active(true)
            .build();
        var unactiveTrainer = Trainer.builder()
            .username(TRAINER_USERNAME)
            .active(false)
            .build();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(unactiveTrainer).when(trainerService).getByUsername(TRAINER_USERNAME);

        assertThrows(TrainerNotActiveException.class, () -> testObject.assign(TRAINEE_USERNAME, TRAINER_USERNAME));

        assertNoUnexpectedInteractions();
    }

    private static Stream<Arguments> provideNullArguments() {
        return Stream.of(
            Arguments.of(null, TRAINER_USERNAME),
            Arguments.of(TRAINEE_USERNAME, null),
            Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullArguments")
    void assign_shouldThrowException_whenArgumentsNull(String traineeUsername, String trainerUsername) {
        assertThrows(NullPointerException.class, () -> testObject.assign(traineeUsername, trainerUsername));

        assertNoUnexpectedInteractions();
    }


    @Test
    void checkAssignExist_shouldDoNothing_whenAssignExist() {
        doReturn(true).when(traineeAssignmentTrainerRepository).checkAssign(TRAINEE_USERNAME, TRAINER_USERNAME);

        testObject.checkAssignExist(TRAINEE_USERNAME, TRAINER_USERNAME);

        assertNoUnexpectedInteractions();
    }

    @Test
    void checkAssignExist_shouldThrowException_whenAssignNotExist() {
        doReturn(false).when(traineeAssignmentTrainerRepository).checkAssign(TRAINEE_USERNAME, TRAINER_USERNAME);

        assertThrows(NotAssignmentException.class, () -> testObject.checkAssignExist(TRAINEE_USERNAME, TRAINER_USERNAME));

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @MethodSource("provideNullArguments")
    void checkAssignExist_shouldThrowException_whenArgumentsNull(String traineeUsername, String trainerUsername) {
        assertThrows(NullPointerException.class, () -> testObject.checkAssignExist(traineeUsername, trainerUsername));

        assertNoUnexpectedInteractions();
    }


    private static Stream<Arguments> provideTestData() {
        return Stream.of(
            Arguments.of(List.of(), 0),
            Arguments.of(List.of(TRAINER_1), 1),
            Arguments.of(List.of(TRAINER_1, TRAINER_2), 2)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void getAssignedTrainers_shouldReturnTrainers(List<Trainer> list, int size) {
        doReturn(list).when(traineeAssignmentTrainerRepository).getAssignedTrainers(TRAINEE_USERNAME);

        var result = testObject.getTrainers(TRAINEE_USERNAME);

        assertEquals(size, result.size());
        assertEquals(list, result);

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void getAssignedTrainers_shouldThrowException_whenArgumentNull(String traineeUsername) {
        assertThrows(NullPointerException.class, () -> testObject.getTrainers(traineeUsername));

        assertNoUnexpectedInteractions();
    }


    @ParameterizedTest
    @MethodSource("provideTestData")
    void getUnassignedTrainers_shouldReturnTrainers(List<Trainer> list, int size) {
        doReturn(list).when(traineeAssignmentTrainerRepository).getUnassignedTrainers(TRAINEE_USERNAME);

        var result = testObject.getUnassignedTrainers(TRAINEE_USERNAME);

        assertEquals(size, result.size());
        assertEquals(list, result);

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void getUnassignedTrainers_shouldThrowException_whenArgumentNull(String traineeUsername) {
        assertThrows(NullPointerException.class, () -> testObject.getUnassignedTrainers(traineeUsername));

        assertNoUnexpectedInteractions();
    }


    void assertNoUnexpectedInteractions() {
        verifyNoMoreInteractions(
            traineeService,
            trainerService,
            traineeAssignmentTrainerRepository
        );
    }
}*/
