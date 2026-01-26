package com.epam.gym.service.generator.name;

import com.epam.gym.GymApplication;
import com.epam.gym.service.generator.name.factory.IUsernameFactory;
import com.epam.gym.service.generator.name.supplier.IUsernameSupplier;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public final class DefaultUserNameGenerator implements IUsernameGenerator {

    private IUsernameFactory usernameFactory;
    private IUsernameSupplier usernameSupplier;

    @Override
    public String generate(@NonNull String firstName, @NonNull String lastName) {
        validateInput(firstName, lastName);
        return Optional.of(usernameSupplier.supply(firstName, lastName))
            .filter(Predicate.not(List::isEmpty))
            .map(this::calculateSuffix)
            .map(suffix -> usernameFactory.create(firstName, lastName, suffix))
            .orElseGet(() -> usernameFactory.create(firstName, lastName));
    }

    private int calculateSuffix(List<String> usernames) {
        return usernames.stream()
            .map(username -> username.split(Pattern.quote(GymApplication.DEFAULT_USERNAME_DELIMITER)))
            .filter(parts -> parts.length == 3)
            .map(parts -> parts[parts.length - 1])
            .mapToInt(Integer::parseInt)
            .max()
            .orElseThrow(() -> new RuntimeException("sdasdfhjdsfgjkhdf")); //todo - update
    }

    private void validateInput(String firstName, String lastName) {
        if (StringUtils.isAnyBlank(firstName, lastName)) {
            throw new IllegalArgumentException("firstName or lastName cannot be blank");
        }
    }

    @Autowired
    public void setUsernameFactory(IUsernameFactory usernameFactory) {
        this.usernameFactory = usernameFactory;
    }

    @Autowired
    public void setUsernameSupplier(IUsernameSupplier usernameSupplier) {
        this.usernameSupplier = usernameSupplier;
    }
}
