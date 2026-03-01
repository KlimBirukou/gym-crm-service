package com.epam.gym.service.trainee;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainee.dto.UpdateTraineeDto;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

public interface ITraineeService {

    Trainee create(@NonNull CreateTraineeDto dto);

    Trainee update(@NonNull UpdateTraineeDto dto);

    void delete(@NonNull String username);

    Trainee getByUsername(@NonNull String username);

    List<Trainee> getByUids(@NonNull List<UUID> uids);
}
