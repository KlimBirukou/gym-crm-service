package com.epam.gym.repository.domain.type;

import com.epam.gym.domain.training.TrainingType;
import com.epam.gym.repository.entity.TrainingTypeEntity;
import com.epam.gym.repository.jpa.type.ITrainingTypeEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class JpaTrainingTypeRepositoryTest {

    private static final String NAME = "name";
    private static final TrainingType TRAINING_TYPE_1 = TrainingType.builder()
        .name(NAME)
        .build();
    private static final TrainingType TRAINING_TYPE_2 = TrainingType.builder()
        .name(NAME)
        .build();
    private static final TrainingTypeEntity TRAINING_TYPE_ENTITY_1 = new TrainingTypeEntity();
    private static final TrainingTypeEntity TRAINING_TYPE_ENTITY_2 = new TrainingTypeEntity();

    @Mock
    private ITrainingTypeEntityRepository repository;
    @Mock
    private ConversionService conversionService;

    private JpaTrainingTypeRepository testObject;

    @BeforeEach
    void setUp() {
        testObject = new JpaTrainingTypeRepository(
            repository,
            conversionService
        );
    }

    @Test
    void getByName_shouldReturnOptionalWithType_whenEntityExist() {
        doReturn(Optional.of(TRAINING_TYPE_ENTITY_1)).when(repository).getByName(NAME);
        doReturn(TRAINING_TYPE_1).when(conversionService).convert(TRAINING_TYPE_ENTITY_1, TrainingType.class);

        var result = testObject.getByName(NAME);

        assertTrue(result.isPresent());
        assertSame(TRAINING_TYPE_1, result.get());

        assertNoUnexpectedInteractions();
    }

    @Test
    void getByName_shouldReturnOptionalEmpty_whenEntityNotExist() {
        doReturn(Optional.empty()).when(repository).getByName(NAME);

        var result = testObject.getByName(NAME);

        assertTrue(result.isEmpty());

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void getByName_shouldThrowException_whenArgumentNull(String name) {
        assertThrows(NullPointerException.class, () -> testObject.getByName(name));

        assertNoUnexpectedInteractions();
    }


    private static Stream<Arguments> provideTestData() {
        return Stream.of(
            Arguments.of(List.of(), List.of()),
            Arguments.of(List.of(TRAINING_TYPE_ENTITY_1), List.of(TRAINING_TYPE_1)),
            Arguments.of(List.of(TRAINING_TYPE_ENTITY_1, TRAINING_TYPE_ENTITY_2), List.of(TRAINING_TYPE_1, TRAINING_TYPE_2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void getAll_shouldReturnTrainingTypes(List<TrainingTypeEntity> entities, List<TrainingType> trainingTypes) {
        doReturn(entities).when(repository).findAll();
        IntStream.range(0, entities.size()).forEach(i -> {
            doReturn(trainingTypes.get(i)).when(conversionService).convert(entities.get(i), TrainingType.class);
        });

        var result = testObject.getAll();

        assertEquals(trainingTypes.size(), result.size());
        assertEquals(trainingTypes, result);

        assertNoUnexpectedInteractions();
    }


    private void assertNoUnexpectedInteractions() {
        verifyNoMoreInteractions(
            repository,
            conversionService
        );
    }
}
