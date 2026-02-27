package com.epam.gym.repository.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import com.epam.gym.repository.entity.TrainerEntity;
import com.epam.gym.domain.user.Trainer;
import lombok.NonNull;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.extensions.spring.DelegatingConverter;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface ITrainerEntityToTrainerMapper extends Converter<@NonNull TrainerEntity, Trainer> {

    @Override
    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "password", source = "user.password")
    @Mapping(target = "active", source = "user.active")
    @Mapping(target = "specialization", source = "specialization")
    Trainer convert(@NonNull TrainerEntity source);

    @InheritInverseConfiguration
    @DelegatingConverter
    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "user.uid", ignore = true)
    @Mapping(target = "user.firstName", source = "firstName")
    @Mapping(target = "user.lastName", source = "lastName")
    @Mapping(target = "user.username", source = "username")
    @Mapping(target = "user.password", source = "password")
    @Mapping(target = "user.active", source = "active")
    @Mapping(target = "specialization", source = "specialization")
    TrainerEntity convert(@NonNull Trainer domain);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "user.uid", ignore = true)
    @Mapping(target = "user.firstName", source = "firstName")
    @Mapping(target = "user.lastName", source = "lastName")
    @Mapping(target = "user.username", ignore = true)
    @Mapping(target = "user.password", source = "password")
    @Mapping(target = "user.active", source = "active")
    @Mapping(target = "specialization", source = "specialization")
    @Mapping(target = "trainings", ignore = true)
    @Mapping(target = "trainees", ignore = true)
    void updateEntity(@NonNull Trainer domain, @MappingTarget @NonNull TrainerEntity entity);
}
