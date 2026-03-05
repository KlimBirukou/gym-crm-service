package com.epam.gym.service.type;

import com.epam.gym.domain.training.TrainingType;
import com.epam.gym.exception.not.found.TrainingTypeNotFoundException;
import com.epam.gym.repository.domain.type.ITrainingTypeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
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

    @Mock
    private ITrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeService testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(trainingTypeRepository);
    }

    @Test
    void getByName_shouldReturnTrainingType_whenTypeExist() {
        var type = buildType(TYPE_NAME_1);
        doReturn(Optional.of(type)).when(trainingTypeRepository).getByName(TYPE_NAME_1);

        var result = testObject.getByName(TYPE_NAME_1);

        assertSame(type, result);
    }

    @Test
    void getByName_shouldThrowException_whenTypeNotExist() {
        doReturn(Optional.empty()).when(trainingTypeRepository).getByName(UNKNOW_TYPE_NAME);

        assertThrows(TrainingTypeNotFoundException.class, () -> testObject.getByName(UNKNOW_TYPE_NAME));
    }

    @ParameterizedTest
    @NullSource
    void getByName_shouldThrowException_whenArgumentNull(String name) {
        assertThrows(NullPointerException.class, () -> testObject.getByName(name));
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
            Arguments.of(List.of(), 0),
            Arguments.of(List.of(buildType(TYPE_NAME_1)), 1),
            Arguments.of(List.of(buildType(TYPE_NAME_1), buildType(TYPE_NAME_2)), 2)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void getAll_shouldReturnList(List<TrainingType> list, int size) {
        doReturn(list).when(trainingTypeRepository).getAll();

        var result = testObject.getAll();

        assertEquals(size, result.size());
        assertEquals(list, result);
    }

    private static TrainingType buildType(String name) {
        return TrainingType.builder()
            .uid(UUID.randomUUID())
            .name(name)
            .build();
    }
}
