package com.epam.gym.crm.repository.domain.type;

import com.epam.gym.crm.domain.training.TrainingType;
import com.epam.gym.crm.repository.jpa.type.ITrainingTypeEntityRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
@RequiredArgsConstructor
public class JpaTrainingTypeRepository implements ITrainingTypeRepository {

    private final ITrainingTypeEntityRepository repository;
    private final ConversionService conversionService;

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainingType> getByName(@NonNull String name) {
        return repository.getByName(name)
            .map(entity -> conversionService.convert(entity, TrainingType.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingType> getAll() {
        return repository.findAll()
            .stream()
            .map(entity -> conversionService.convert(entity, TrainingType.class))
            .toList();
    }
}
