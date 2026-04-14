package com.epam.gym.crm.service.trainer;

import com.epam.gym.crm.exception.not.found.TrainerNotFoundException;
import com.epam.gym.crm.service.auth.password.IPasswordService;
import com.epam.gym.crm.service.generator.name.IUsernameGenerator;
import com.epam.gym.crm.service.generator.password.IPasswordGenerator;
import com.epam.gym.crm.domain.user.Trainer;
import com.epam.gym.crm.repository.domain.trainer.ITrainerRepository;
import com.epam.gym.crm.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.crm.service.trainer.dto.UpdateTrainerDto;
import com.epam.gym.crm.service.type.ITrainingTypeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class TrainerService implements ITrainerService {

    private final IPasswordGenerator passwordGenerator;
    private final IUsernameGenerator usernameGenerator;
    private final ITrainerRepository trainerRepository;
    private final ITrainingTypeService trainingTypeService;
    private final IPasswordService passwordService;

    @Override
    @Transactional
    public Trainer create(@NonNull CreateTrainerDto dto) {
        var password = passwordGenerator.generate();
        var trainer = Trainer.builder()
            .uid(UUID.randomUUID())
            .firstName(dto.firstName())
            .lastName(dto.lastName())
            .username(usernameGenerator.generate(dto.firstName(), dto.lastName()))
            .password(passwordService.hashPassword(password))
            .specialization(trainingTypeService.getByName(dto.specialization()))
            .active(true)
            .build();
        trainerRepository.save(trainer);
        trainer.setPassword(password);
        return trainer;
    }

    @Override
    @Transactional
    public Trainer update(@NonNull UpdateTrainerDto dto) {
        var trainer = trainerRepository.getByUsername(dto.username())
            .orElseThrow(() -> new TrainerNotFoundException(dto.username()));
        trainer.setFirstName(dto.firstName());
        trainer.setLastName(dto.lastName());
        trainerRepository.update(trainer);
        return trainer;
    }

    @Override
    @Transactional(readOnly = true)
    public Trainer getByUsername(@NonNull String username) {
        return trainerRepository.getByUsername(username)
            .orElseThrow(() -> new TrainerNotFoundException(username));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> getByUids(@NonNull List<UUID> uids) {
        return Optional.of(uids)
            .filter(Predicate.not(Collection::isEmpty))
            .map(trainerRepository::findAllByUids)
            .orElseGet(List::of);
    }
}
