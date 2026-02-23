package com.epam.gym.facade;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.service.trainee.ITraineeService;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Setter(onMethod_ = @Autowired)
public final class GymFacade implements IGymFacade {

    private ITraineeService traineeService;

    public Trainee createTrainee(@NonNull CreateTraineeDto dto) {
        log.info("Create Trainee. Started. Firstname=[%s], Lastname = [%s]"
            .formatted(dto.firstName(), dto.lastName()));
        var trainee = traineeService.create(dto);
        log.info("Create Trainee. Finished. Firstname=[%S], Lastname=[%S], Username=[%S]"
            .formatted(trainee.getFirstName(), trainee.getLastName(), trainee.getUsername()));
        return trainee;
    }
}
