package com.epam.gym.service.assignment;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.service.assignment.dto.AssignDto;
import lombok.NonNull;

import java.util.List;

public interface ITraineeAssignmentTrainerService {

    void assign(@NonNull AssignDto dto);

    void checkAssign(@NonNull AssignDto dto);

    List<Trainer> getAssignedTrainers(@NonNull String username);

    List<Trainer> getUnassignedTrainers(@NonNull String username);
}
