package com.epam.gym.crm.repository.domain.trainee;

import com.epam.gym.crm.domain.user.Trainee;
import com.epam.gym.crm.exception.not.found.TraineeNotFoundException;
import com.epam.gym.crm.repository.entity.TraineeEntity;
import com.epam.gym.crm.repository.jpa.trainee.ITraineeEntityRepository;
import com.epam.gym.crm.repository.mapper.ITraineeEntityToTraineeMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class JpaTraineeRepositoryTest {

    private static final UUID UID_1 = UUID.randomUUID();
    private static final UUID UID_2 = UUID.randomUUID();
    private static final String USERNAME = "username";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final Trainee TRAINEE_1 = Trainee.builder().username(USERNAME).build();
    private static final Trainee TRAINEE_2 = Trainee.builder().username(USERNAME).build();
    private static final TraineeEntity TRAINEE_ENTITY_1 = new TraineeEntity();
    private static final TraineeEntity TRAINEE_ENTITY_2 = new TraineeEntity();

    @Mock
    private ITraineeEntityRepository repository;
    @Mock
    private ConversionService conversionService;
    @Mock
    private ITraineeEntityToTraineeMapper mapper;

    @InjectMocks
    private JpaTraineeRepository testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(repository, conversionService, mapper);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void existByUsername_shouldReturnBoolean_whenArgumentNotNull(boolean isExist) {
        doReturn(isExist).when(repository).existsByUserUsername(USERNAME);

        var result = testObject.existsByUsername(USERNAME);

        assertEquals(isExist, result);
    }

    @ParameterizedTest
    @NullSource
    void existByUsername_shouldThrowException_whenArgumentNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.existsByUsername(username));
    }

    @Test
    void getByUsername_shouldReturnOptionalWithTrainee_whenEntityExist() {
        doReturn(Optional.of(TRAINEE_ENTITY_1)).when(repository).findByUserUsername(USERNAME);
        doReturn(TRAINEE_1).when(conversionService).convert(TRAINEE_ENTITY_1, Trainee.class);

        var result = testObject.getByUsername(USERNAME);

        assertTrue(result.isPresent());
        assertSame(TRAINEE_1, result.get());
    }

    @Test
    void getByUsername_shouldReturnOptionalEmpty_whenEntityNotExist() {
        doReturn(Optional.empty()).when(repository).findByUserUsername(USERNAME);

        var result = testObject.getByUsername(USERNAME);

        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    void getByUsername_shouldThrowException_whenArgumentNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.getByUsername(username));

    }

    @Test
    void save_shouldSaveEntity() {
        doReturn(TRAINEE_ENTITY_1).when(conversionService).convert(TRAINEE_1, TraineeEntity.class);

        testObject.save(TRAINEE_1);

        verify(repository).save(TRAINEE_ENTITY_1);
    }

    @ParameterizedTest
    @NullSource
    void save_shouldThrowException_whenArgumentNull(Trainee trainee) {
        assertThrows(NullPointerException.class, () -> testObject.save(trainee));
    }

    @Test
    void update_shouldUpdateEntity_whenTraineeExists() {
        doReturn(Optional.of(TRAINEE_ENTITY_1)).when(repository).findByUserUsername(USERNAME);

        testObject.update(TRAINEE_1);

        verify(mapper).updateEntity(TRAINEE_1, TRAINEE_ENTITY_1);
        verify(repository).save(TRAINEE_ENTITY_1);
    }

    @Test
    void update_shouldThrowException_whenTraineeNotExists() {
        doReturn(Optional.empty()).when(repository).findByUserUsername(USERNAME);

        assertThrows(TraineeNotFoundException.class, () -> testObject.update(TRAINEE_1));
    }

    @ParameterizedTest
    @NullSource
    void update_shouldThrowException_whenArgumentNull(Trainee trainee) {
        assertThrows(NullPointerException.class, () -> testObject.update(trainee));
    }

    @Test
    void delete_shouldDeleteEntity() {
        doNothing().when(repository).deleteByUserUsername(USERNAME);

        testObject.deleteByUsername(USERNAME);

        verify(repository).deleteByUserUsername(USERNAME);
    }

    @ParameterizedTest
    @NullSource
    void delete_shouldThrowException_whenArgumentNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.deleteByUsername(username));
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
            Arguments.of(List.of(), List.of()),
            Arguments.of(List.of(TRAINEE_ENTITY_1), List.of(TRAINEE_1)),
            Arguments.of(List.of(TRAINEE_ENTITY_1, TRAINEE_ENTITY_2), List.of(TRAINEE_1, TRAINEE_2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void getByFirstNameAndLastName_shouldReturnTrainees(List<TraineeEntity> entities, List<Trainee> trainees) {
        doReturn(entities).when(repository).findByUserFirstNameAndUserLastName(FIRSTNAME, LASTNAME);
        entities.forEach(entity -> doReturn(new Trainee()).when(conversionService)
            .convert(entity, Trainee.class)
        );

        var result = testObject.getByFirstNameAndLastName(FIRSTNAME, LASTNAME);

        assertEquals(trainees.size(), result.size());
        verify(conversionService, times(entities.size())).convert(any(TraineeEntity.class), eq(Trainee.class));
    }

    private static Stream<Arguments> provideNullArguments() {
        return Stream.of(
            Arguments.of(null, FIRSTNAME),
            Arguments.of(LASTNAME, null),
            Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullArguments")
    void getByFirstNameAndLastName_shouldThrowException_whenArgumentsNull(String firstname, String lastname) {
        assertThrows(NullPointerException.class, () -> testObject.getByFirstNameAndLastName(firstname, lastname));
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void findAllByUids_shouldReturnTrainees(List<TraineeEntity> entities, List<Trainee> trainees) {
        var uids = List.of(UID_1, UID_2);
        doReturn(entities).when(repository).findAllByUidIn(uids);
        entities.forEach(entity -> doReturn(new Trainee()).when(conversionService)
            .convert(entity, Trainee.class)
        );

        var result = testObject.findAllByUids(uids);

        assertEquals(trainees.size(), result.size());
        verify(conversionService, times(entities.size())).convert(any(TraineeEntity.class), eq(Trainee.class));
    }

    @ParameterizedTest
    @NullSource
    void findAllByUids_shouldThrowException_whenArgumentNull(List<UUID> uids) {
        assertThrows(NullPointerException.class, () -> testObject.findAllByUids(uids));
    }
}
