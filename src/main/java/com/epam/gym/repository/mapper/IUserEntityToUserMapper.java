package com.epam.gym.repository.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import com.epam.gym.domain.user.User;
import com.epam.gym.repository.entity.UserEntity;
import lombok.NonNull;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.extensions.spring.DelegatingConverter;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface IUserEntityToUserMapper extends Converter<@NonNull UserEntity, User> {

    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "active", source = "active")
    User convert(@NonNull UserEntity source);

    @InheritInverseConfiguration
    @DelegatingConverter
    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "trainee", ignore = true)
    @Mapping(target = "trainer", ignore = true)
    UserEntity convert(@NonNull User domain);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", source = "password")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "trainee", ignore = true)
    @Mapping(target = "trainer", ignore = true)
    void updateEntity(@NonNull User domain, @MappingTarget UserEntity entity);
}
