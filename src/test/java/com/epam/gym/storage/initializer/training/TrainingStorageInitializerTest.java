package com.epam.gym.storage.initializer.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.mother.TrainingMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingStorageInitializerTest {

    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);
    private static final UUID UID = UUID.randomUUID();
    private static final Training TRAINING  = TrainingMother.get(UID, DATE);
    private static final List<Training> TRAININGS = List.of(TRAINING);
    private static final String JSON_CONTENT = "[]";

    @Mock
    private Resource dataFile;
    @Mock
    private ObjectMapper objectMapper;

    private TrainingStorageInitializer testObject;

    @BeforeEach
    void setUp() {
        testObject = new TrainingStorageInitializer();
        testObject.setObjectMapper(objectMapper);
    }

    @Test
    void load_shouldLoadTrainings_whenFileExistsAndValid() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(JSON_CONTENT.getBytes());
        when(dataFile.exists()).thenReturn(true);
        when(dataFile.getInputStream()).thenReturn(inputStream);

        doReturn(TRAININGS).when(objectMapper)
            .readValue(eq(inputStream), any(TypeReference.class));

        var result = testObject.load(dataFile);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TRAINING, result.getFirst());

        verify(dataFile, times(1))
            .exists();
        verify(dataFile, times(1))
            .getInputStream();
        verify(objectMapper, times(1))
            .readValue(eq(inputStream), any(TypeReference.class));
    }

    @Test
    void load_shouldReturnEmptyList_whenFileExistsButEmpty() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(JSON_CONTENT.getBytes());
        when(dataFile.exists()).thenReturn(true);
        when(dataFile.getInputStream()).thenReturn(inputStream);
        doReturn(List.of()).when(objectMapper)
            .readValue(eq(inputStream), any(TypeReference.class));

        var result = testObject.load(dataFile);

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(dataFile, times(1))
            .exists();
        verify(dataFile, times(1))
            .getInputStream();
        verify(objectMapper, times(1))
            .readValue(eq(inputStream), any(TypeReference.class));
    }

    @Test
    void load_shouldThrowException_whenFileNotExists() {
        when(dataFile.exists()).thenReturn(false);

        assertThrows(RuntimeException.class,
            () -> testObject.load(dataFile));

        verify(dataFile, times(1)).exists();
    }

    @Test
    void load_shouldThrowException_whenIOExceptionOccurs() throws IOException {
        when(dataFile.exists()).thenReturn(true);
        doThrow(new IOException()).when(dataFile).getInputStream();

        assertThrows(RuntimeException.class,
            () -> testObject.load(dataFile));

        verify(dataFile, times(1))
            .exists();
        verify(dataFile, times(1))
            .getInputStream();
    }

    @Test
    void load_shouldThrowException_whenParsingFails() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(JSON_CONTENT.getBytes());
        when(dataFile.exists()).thenReturn(true);
        when(dataFile.getInputStream()).thenReturn(inputStream);
        doThrow(new RuntimeException()).when(objectMapper)
            .readValue(eq(inputStream), any(TypeReference.class));

        assertThrows(RuntimeException.class,
            () -> testObject.load(dataFile));

        verify(dataFile, times(1))
            .exists();
        verify(dataFile, times(1))
            .getInputStream();
        verify(objectMapper, times(1))
            .readValue(eq(inputStream), any(TypeReference.class));
    }

    @ParameterizedTest
    @NullSource
    void load_shouldThrowException_whenDataFileNull(Resource resource) {
        assertThrows(NullPointerException.class,
            () -> testObject.load(resource));
    }
}