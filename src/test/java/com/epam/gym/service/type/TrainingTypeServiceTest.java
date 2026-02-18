package com.epam.gym.service.type;

import com.epam.gym.domain.training.TrainingType;
import com.epam.gym.exception.not.found.TrainingTypeNotFoundException;
import com.epam.gym.repository.domain.type.ITrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceTest {

    private static final String TYPE_NAME_1 = "type_name_1";
    private static final String TYPE_NAME_2 = "type_name_2";
    private static final String UNKNOW_TYPE_NAME = "unknow_type_name";

    private static final TrainingType TRAINING_TYPE_1 = TrainingType.builder()
        .uid(UUID.randomUUID())
        .name(TYPE_NAME_1)
        .build();
    private static final TrainingType TRAINING_TYPE_2 = TrainingType.builder()
        .uid(UUID.randomUUID())
        .name(TYPE_NAME_2)
        .build();

    @Mock
    private ITrainingTypeRepository trainingTypeRepository;

    private TrainingTypeService testObject;

    @BeforeEach
    void setUp() {
        testObject = new TrainingTypeService(trainingTypeRepository);
    }

    @Test
    void getByName_shouldReturnTrainingType_whenTypeExist() {
        doReturn(Optional.of(TRAINING_TYPE_1)).when(trainingTypeRepository).getByName(TYPE_NAME_1);

        var result = testObject.getByName(TYPE_NAME_1);

        assertSame(TRAINING_TYPE_1, result);

        verifyNoMoreInteractions(trainingTypeRepository);
    }

    @Test
    void getByName_shouldThrowException_whenTypeNotExist() {
        doReturn(Optional.empty()).when(trainingTypeRepository).getByName(UNKNOW_TYPE_NAME);

        assertThrows(TrainingTypeNotFoundException.class, () -> testObject.getByName(UNKNOW_TYPE_NAME));

        verifyNoMoreInteractions(trainingTypeRepository);
    }

    @ParameterizedTest
    @NullSource
    void getByName_shouldThrowException_whenArgumentNull(String name) {
        assertThrows(NullPointerException.class, () -> testObject.getByName(name));

        verifyNoMoreInteractions(trainingTypeRepository);
    }


    private static Stream<Arguments> provideTestData() {
        return Stream.of(
            Arguments.of(List.of(), 0),
            Arguments.of(List.of(TRAINING_TYPE_1), 1),
            Arguments.of(List.of(TRAINING_TYPE_1, TRAINING_TYPE_2), 2)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void getAll_shouldReturnList(List<TrainingType> list, int size) {
        doReturn(list).when(trainingTypeRepository).getAll();

        var result = testObject.getAll();

        assertEquals(size, result.size());
        assertEquals(list, result);

        verifyNoMoreInteractions(trainingTypeRepository);
    }
}
