package com.epam.gym.domain.training;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public final class Training {

    private UUID uid;
    private UUID traineeUid;
    private UUID trainerUid;
    private String name;
    private TrainingType type;
    private LocalDate date;
    private Duration duration;
}
