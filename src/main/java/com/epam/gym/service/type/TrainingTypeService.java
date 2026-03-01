package com.epam.gym.service.type;

import com.epam.gym.domain.training.TrainingType;
import com.epam.gym.exception.not.found.TrainingTypeNotFoundException;
import com.epam.gym.repository.domain.type.ITrainingTypeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingTypeService implements ITrainingTypeService {

    private final ITrainingTypeRepository trainingTypeRepository;

    @Override
    @Transactional
    public TrainingType getByName(@NonNull String name) {
        return trainingTypeRepository.getByName(name)
            .orElseThrow(() -> new TrainingTypeNotFoundException(name));
    }

    @Override
    @Transactional
    public List<TrainingType> getAll() {
        return trainingTypeRepository.getAll();
    }
}
