package com.epam.gym.crm.sender;

import com.epam.gym.crm.configuration.properties.RequestUidProperties;
import com.epam.gym.crm.domain.training.Training;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadUpdateSenderTest {

    private static final String TRAINER_USERNAME = "trainer_username";
    private static final String TOPIC = "topic";
    private static final String MDC_KEY = "requestUid";
    private static final String HEADER_NAME = "X-Request-Id";
    private static final LocalDate DATE = LocalDate.of(2026, 6, 6);
    private static final int DURATION_MINUTES = 120;

    @Mock
    private KafkaTemplate<String, TrainerWorkloadUpdateEvent> kafkaTemplate;
    @Mock
    private RequestUidProperties requestUidProperties;

    @Captor
    private ArgumentCaptor<ProducerRecord<String, TrainerWorkloadUpdateEvent>> recordCaptor;

    @InjectMocks
    private TrainerWorkloadUpdateEventSender testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(kafkaTemplate, requestUidProperties);
    }

    @Test
    void notify_shouldSendRecord_whenValidInput() {
        setupMocks();

        testObject.notify(buildTraining(), TRAINER_USERNAME, EventType.ADD);

        verify(kafkaTemplate).send(recordCaptor.capture());
        var payload = recordCaptor.getValue().value();

        assertEquals(TOPIC, recordCaptor.getValue().topic());
        assertEquals(TRAINER_USERNAME, recordCaptor.getValue().key());
        assertEquals(TRAINER_USERNAME, payload.trainerUsername());
        assertEquals(DATE, payload.trainingDate());
        assertEquals(DURATION_MINUTES, payload.trainingDuration());
        assertEquals(EventType.ADD, payload.actionType());
    }

    private static Stream<Arguments> provideNullArguments() {
        return Stream.of(
            Arguments.of(null, TRAINER_USERNAME, EventType.ADD),
            Arguments.of(buildTraining(), null, EventType.ADD),
            Arguments.of(buildTraining(), TRAINER_USERNAME, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullArguments")
    void notify_shouldThrowException_whenArgumentIsNull(Training training,
                                                     String trainerUsername,
                                                     EventType actionType) {
        assertThrows(NullPointerException.class,
            () -> testObject.notify(training, trainerUsername, actionType));
    }

    private void setupMocks() {
        ReflectionTestUtils.setField(testObject, "trainerWorkloadTopic", TOPIC);
        doReturn(MDC_KEY).when(requestUidProperties).mdcKey();
        doReturn(HEADER_NAME).when(requestUidProperties).headerName();
        doReturn(CompletableFuture.completedFuture(null)).when(kafkaTemplate).send(any(ProducerRecord.class));
    }

    private static Training buildTraining() {
        return Training.builder()
            .date(DATE)
            .duration(Duration.ofMinutes(DURATION_MINUTES))
            .build();
    }
}
