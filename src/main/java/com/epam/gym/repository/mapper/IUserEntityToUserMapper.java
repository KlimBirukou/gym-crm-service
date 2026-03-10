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

    @Override
    User convert(UserEntity source);

    @InheritInverseConfiguration
    @DelegatingConverter
    @Mapping(target = "trainee", ignore = true)
    @Mapping(target = "trainer", ignore = true)
    UserEntity convert(User domain);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "trainee", ignore = true)
    @Mapping(target = "trainer", ignore = true)
    void updateEntity(User domain, @MappingTarget UserEntity entity);
}
