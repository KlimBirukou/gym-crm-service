package com.epam.gym.repository.user.user;

import com.epam.gym.domain.user.User;
import com.epam.gym.repository.jpa.repository.IUserEntityRepository;
import com.epam.gym.repository.jpa.entity.UserEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
@RequiredArgsConstructor
public class JpaUserRepository implements IUserRepository {

    private final IUserEntityRepository userEntityRepository;
    private final ConversionService conversionService;

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(@NonNull String username) {
        return userEntityRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public Optional<User> findByUid(@NonNull UUID uid) {
        return userEntityRepository.findById(uid)
            .map(entity -> conversionService.convert(entity, User.class));
    }

    @Override
    @Transactional
    public Optional<User> findByUsername(@NonNull String username) {
        return userEntityRepository.findByUsername(username)
            .map(entity -> conversionService.convert(entity, User.class));
    }

    @Override
    @Transactional
    public void save(@NonNull User user) {
        var entity = conversionService.convert(user, UserEntity.class);
        if (Objects.isNull(entity)) {
            throw new IllegalStateException("Failed conversion User to UserEntity");
        }
        userEntityRepository.save(entity);
    }
}
