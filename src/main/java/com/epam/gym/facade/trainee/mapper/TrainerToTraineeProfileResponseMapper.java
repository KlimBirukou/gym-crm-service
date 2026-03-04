package com.epam.gym.facade.trainee.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import com.epam.gym.controller.rest.trainee.dto.response.TrainerProfileResponse;
import com.epam.gym.domain.user.Trainer;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface TrainerToTraineeProfileResponseMapper
    extends Converter<@NonNull Trainer, TrainerProfileResponse> {

    @Override
    @Mapping(target = "username", source = "username")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "specializationName", source = "specialization.name")
    TrainerProfileResponse convert(@NonNull Trainer source);
}
