package com.epam.gym.service.generator.name;

import com.epam.gym.GymApplication;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.trainee.ITraineeRepository;
import com.epam.gym.repository.trainer.ITrainerRepository;
import com.epam.gym.service.generator.name.factory.IUsernameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public final class DefaultUserNameGenerator implements IUsernameGenerator {

    private ITraineeRepository traineeRepository;
    private ITrainerRepository trainerRepository;
    private IUsernameFactory usernameFactory;

    @Override
    public String generate(String firstName, String lastName) {
        var traineesUsernames = traineeRepository.findByFirstNameAndLastName(firstName, lastName)
            .stream()
            .map(Trainee::getUsername);
        var trainersUsernames = trainerRepository.findByFirstNameAndLastName(firstName, lastName)
            .stream()
            .map(Trainer::getUsername);
        var lastSuffix = Stream.concat(traineesUsernames, trainersUsernames)
            .map(username -> username.split(Pattern.quote(GymApplication.DEFAULT_USERNAME_DELIMITER)))
            .filter(parts -> parts.length > 2)
            .map(parts -> parts[parts.length - 1])
            .mapToInt(Integer::parseInt)
            .max();
        return lastSuffix.isEmpty()
            ? usernameFactory.create(firstName, lastName)
            : usernameFactory.create(firstName, lastName, lastSuffix.getAsInt());
    }

    @Autowired
    public void setTraineeRepository(ITraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
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
