package com.epam.gym.repository.domain.auth;

import com.epam.gym.domain.auth.LoginAttempt;
import com.epam.gym.repository.entity.LoginAttemptEntity;
import com.epam.gym.repository.jpa.auth.ILoginAttemptsEntityRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaLoginAttemptRepository implements ILoginAttemptRepository {

    private final ILoginAttemptsEntityRepository repository;
    private final ConversionService conversionService;

    @Override
    @Transactional(readOnly = true)
    public Optional<LoginAttempt> findByUserUid(@NonNull UUID userUid) {
        return repository.findById(userUid)
            .map(entity -> conversionService.convert(entity, LoginAttempt.class));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(@NonNull LoginAttempt loginAttempt) {
        var entity = conversionService.convert(loginAttempt, LoginAttemptEntity.class);
        repository.save(entity);
    }

    @Override
    @Transactional
    public void deleteByUserUid(@NonNull UUID userUid) {
        repository.deleteById(userUid);
    }
}
