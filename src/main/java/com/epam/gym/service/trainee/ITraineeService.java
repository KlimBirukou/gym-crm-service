package com.epam.gym.service.trainee;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.service.trainee.dto.ChangePasswordDto;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainee.dto.UpdateTraineeDto;
import lombok.NonNull;

public interface ITraineeService {

    Trainee create(@NonNull CreateTraineeDto dto);

    void update(@NonNull UpdateTraineeDto dto);

    void changePassword(@NonNull ChangePasswordDto dto);

    void toggleStatus(@NonNull String username);

    void delete(@NonNull String username);

    Trainee getByUsername(String username);
}
