package com.epam.gym.domain.user;

import com.epam.gym.domain.training.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public final class Trainer {

    private UUID uid;
    private User user;
    private TrainingType specialization;
}
