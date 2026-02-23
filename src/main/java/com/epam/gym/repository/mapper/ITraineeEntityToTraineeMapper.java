package com.epam.gym.repository.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import com.epam.gym.repository.entity.TraineeEntity;
import com.epam.gym.domain.user.Trainee;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface ITraineeEntityToTraineeMapper extends Converter<@NonNull TraineeEntity, Trainee> {

    @Override
    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "password", source = "user.password")
    @Mapping(target = "active", source = "user.active")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "birthdate", source = "birthdate")
    Trainee convert(@NonNull TraineeEntity entity);

    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "user.uid", ignore = true)
    @Mapping(target = "user.firstName", source = "firstName")
    @Mapping(target = "user.lastName", source = "lastName")
    @Mapping(target = "user.username", source = "username")
    @Mapping(target = "user.password", source = "password")
    @Mapping(target = "user.active", source = "active")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "birthdate", source = "birthdate")
    TraineeEntity invertConvert(@NonNull Trainee trainee);
}
