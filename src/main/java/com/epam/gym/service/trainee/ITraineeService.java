package com.epam.gym.service.trainee;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainee.dto.UpdateTraineeDto;

import java.util.UUID;

public interface ITraineeService {

    Trainee create(CreateTraineeDto dto);

    void update(UpdateTraineeDto dto);

    void delete(UUID uid);
}
