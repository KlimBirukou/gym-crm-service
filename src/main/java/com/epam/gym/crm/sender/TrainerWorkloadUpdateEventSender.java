package com.epam.gym.crm.sender;

import com.epam.gym.crm.configuration.properties.RequestUidProperties;
import com.epam.gym.crm.domain.training.Training;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerWorkloadUpdateEventSender implements ITrainerWorkloadUpdateEventSender {

    private final KafkaTemplate<String, TrainerWorkloadUpdateEvent> kafkaTemplate;
    private final RequestUidProperties requestUidProperties;

    @Value("${application.messaging.topics.trainer-workload}")
    private String trainerWorkloadTopic;

    @Override
    public void notify(@NonNull Training training,
                       @NonNull String trainerUsername,
                       @NonNull EventType actionType) {
        log.info(
            "Notify workload service. Started. Trainer={}, Duration={}, Date={}, ActionType={}",
            trainerUsername, training.getDuration(), training.getDate(), actionType
        );
        var request = TrainerWorkloadUpdateEvent.builder()
            .trainerUsername(trainerUsername)
            .trainingDate(training.getDate())
            .trainingDuration((int) training.getDuration().toMinutes())
            .actionType(actionType)
            .build();
        send(trainerUsername, request);
    }

    private void send(@NonNull String trainerUsername, TrainerWorkloadUpdateEvent request) {
        kafkaTemplate.send(createRecord(trainerUsername, request))
            .thenAccept(result ->
                log.info("Notify workload service. Finished. Trainer={}, Partition={}, Offset={}",
                    trainerUsername, result.getRecordMetadata().partition(), result.getRecordMetadata().offset()
                )
            )
            .join();
    }

    private ProducerRecord<String, TrainerWorkloadUpdateEvent> createRecord(String key, TrainerWorkloadUpdateEvent payload) {
        var headerValue = Optional.ofNullable(MDC.get(requestUidProperties.mdcKey()))
            .orElse("")
            .getBytes(StandardCharsets.UTF_8);
        var producerRecord = new ProducerRecord<>(trainerWorkloadTopic, key, payload);
        producerRecord.headers().add(requestUidProperties.headerName(), headerValue);
        return producerRecord;
    }
}
