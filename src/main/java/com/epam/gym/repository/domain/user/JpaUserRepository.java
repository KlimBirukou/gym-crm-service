package com.epam.gym.repository.domain.user;

import com.epam.gym.domain.user.User;
import com.epam.gym.exception.not.found.UserNotFoundException;
import com.epam.gym.repository.entity.UserEntity;
import com.epam.gym.repository.jpa.user.IUserEntityRepository;
import com.epam.gym.repository.mapper.IUserEntityToUserMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Primary
@RequiredArgsConstructor
public class JpaUserRepository implements IUserRepository {

    private final IUserEntityRepository repository;
    private final ConversionService conversionService;
    private final IUserEntityToUserMapper mapper;

    @Override
    @Transactional
    public Optional<User> getByUsername(@NonNull String username) {
        return repository.findByUsername(username)
            .map(entity -> conversionService.convert(entity, User.class));
    }

    @Override
    @Transactional
    public void save(@NonNull User user) {
        var entity = conversionService.convert(user, UserEntity.class);
        repository.save(entity);
    }

    @Override
    @Transactional
    public void update(@NonNull User user) {
        var entity = repository.findByUsername(user.getUsername())
            .orElseThrow(() -> new UserNotFoundException(user.getUsername()));
        mapper.updateEntity(user, entity);
        repository.save(entity);
    }
}
