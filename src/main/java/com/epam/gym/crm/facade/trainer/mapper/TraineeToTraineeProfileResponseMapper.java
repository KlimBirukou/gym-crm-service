package com.epam.gym.crm.facade.trainer.mapper;

import com.epam.gym.crm.configuration.IMapStructConfiguration;
import com.epam.gym.crm.controller.rest.trainer.dto.response.TraineeProfileResponse;
import com.epam.gym.crm.domain.user.Trainee;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface TraineeToTraineeProfileResponseMapper
    extends Converter<@NonNull Trainee, TraineeProfileResponse> {

    @Override
    TraineeProfileResponse convert(Trainee source);
}
