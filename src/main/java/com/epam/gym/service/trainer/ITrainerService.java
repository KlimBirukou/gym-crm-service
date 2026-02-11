package com.epam.gym.service.trainer;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import com.epam.gym.service.user.dto.ChangePasswordDto;
import com.epam.gym.service.user.dto.ToggleStatusDto;
import lombok.NonNull;

public interface ITrainerService {

    Trainer create(@NonNull CreateTrainerDto dto);

    void update(@NonNull UpdateTrainerDto dto);

    Trainer findByUsername(@NonNull String username);

    void changePassword(@NonNull ChangePasswordDto dto);

    void toggleStatus(@NonNull ToggleStatusDto dto);
}
