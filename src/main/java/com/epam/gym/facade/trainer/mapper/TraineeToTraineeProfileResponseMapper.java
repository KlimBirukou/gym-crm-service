package com.epam.gym.facade.trainer.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import com.epam.gym.controller.rest.trainer.dto.response.TraineeProfileResponse;
import com.epam.gym.domain.user.Trainee;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface TraineeToTraineeProfileResponseMapper
    extends Converter<@NonNull Trainee, TraineeProfileResponse> {

    @Override
    TraineeProfileResponse convert(Trainee source);
}
