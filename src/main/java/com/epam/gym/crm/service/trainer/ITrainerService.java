package com.epam.gym.crm.service.trainer;

import com.epam.gym.crm.domain.user.Trainer;
import com.epam.gym.crm.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.crm.service.trainer.dto.UpdateTrainerDto;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

public interface ITrainerService {

    Trainer create(@NonNull CreateTrainerDto dto);

    Trainer update(@NonNull UpdateTrainerDto dto);

    Trainer getByUsername(@NonNull String username);

    List<Trainer> getByUids(@NonNull List<UUID> uids);
}
