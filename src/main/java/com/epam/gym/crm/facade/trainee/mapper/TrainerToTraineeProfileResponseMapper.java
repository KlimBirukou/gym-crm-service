package com.epam.gym.crm.facade.trainee.mapper;

import com.epam.gym.crm.configuration.IMapStructConfiguration;
import com.epam.gym.crm.controller.rest.trainee.dto.response.TrainerProfileResponse;
import com.epam.gym.crm.domain.user.Trainer;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface TrainerToTraineeProfileResponseMapper
    extends Converter<@NonNull Trainer, TrainerProfileResponse> {

    @Override
    @Mapping(target = "specializationName", source = "specialization.name")
    TrainerProfileResponse convert(Trainer source);
}
