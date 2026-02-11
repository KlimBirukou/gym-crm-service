package com.epam.gym.repository.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import com.epam.gym.domain.user.User;
import com.epam.gym.repository.jpa.entity.UserEntity;
import lombok.NonNull;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface UserEntityToUserMapper extends Converter<@NonNull UserEntity, User> {

    @Override
    User convert(UserEntity entity);

    @InheritInverseConfiguration
    UserEntity invertConvert(User user);
}
