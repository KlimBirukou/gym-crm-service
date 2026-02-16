package com.epam.gym.service.trainer;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.service.trainer.dto.ChangePasswordDto;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import lombok.NonNull;

public interface ITrainerService {

    Trainer create(@NonNull CreateTrainerDto dto);

    void update(@NonNull UpdateTrainerDto dto);

    void changePassword(@NonNull ChangePasswordDto dto);

    void toggleStatus(@NonNull String username);

    Trainer getByUsername(String username);
}
