package com.epam.gym.service.name;

import com.epam.gym.GymApplication;
import com.epam.gym.domain.Trainer;
import com.epam.gym.repository.trainer.ITrainerRepository;
import com.epam.gym.service.name.factory.IUsernameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class DefaultUserNameGenerator implements IUsernameGenerator {

    private ITrainerRepository trainerRepository;
    private IUsernameFactory usernameFactory;

    @Override
    public String generate(String firstName, String lastName) {
        var lastSuffix = trainerRepository.findByFirstNameAndLastName(firstName, lastName)
            .stream()
            .map(Trainer::getUsername)
            .map(username -> username.split(GymApplication.DEFAULT_USERNAME_DELIMITER))
            .filter(parts -> parts.length > 2)
            .map(parts -> parts[parts.length - 1])
            .mapToInt(Integer::parseInt)
            .max();
        return lastSuffix.isEmpty()
            ? usernameFactory.create(firstName, lastName)
            : usernameFactory.create(firstName, lastName, lastSuffix.getAsInt());
    }

    @Autowired
    public void setTrainerRepository(ITrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Autowired
    public void setUsernameFactory(IUsernameFactory usernameFactory) {
        this.usernameFactory = usernameFactory;
    }
}
