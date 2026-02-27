package com.epam.gym.facade.trainer.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import com.epam.gym.controller.rest.trainer.dto.response.ShortUserProfileResponse;
import com.epam.gym.domain.user.Trainee;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface TraineeToShortTraineeResponseMapper
    extends Converter<@NonNull Trainee, ShortUserProfileResponse> {

    @Override
    @Mapping(target = "username", source = "username")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    ShortUserProfileResponse convert(@NonNull Trainee source);
}
