package com.epam.gym.service.trainee;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainee.dto.UpdateTraineeDto;
import lombok.NonNull;

import java.util.UUID;

public interface ITraineeService {

    Trainee create(@NonNull CreateTraineeDto dto);

    void update(@NonNull UpdateTraineeDto dto);

    void delete(@NonNull UUID uid);
}
