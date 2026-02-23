package com.epam.gym.facade;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import lombok.NonNull;

public interface IGymFacade {

    Trainee createTrainee(@NonNull CreateTraineeDto dto);
}
