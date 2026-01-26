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
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public final class DefaultUsernameGenerator implements IUsernameGenerator {

    private IUsernameFactory usernameFactory;
    private IUsernameSupplier usernameSupplier;

    @Override
    public String generate(@NonNull String firstName, @NonNull String lastName) {
        validateInput(firstName, lastName);
        var usernames = usernameSupplier.supply(firstName, lastName);
        if (usernames.isEmpty()) {
            return usernameFactory.create(firstName, lastName);
        }
        return usernames.stream()
            .map(username -> username.split(Pattern.quote(GymApplication.DEFAULT_USERNAME_DELIMITER)))
            .filter(parts -> parts.length == 3)
            .mapToInt(parts -> Integer.parseInt(parts[2]))
            .max()
            .stream()
            .mapToObj(maxSuffix -> usernameFactory.create(firstName, lastName, maxSuffix))
            .findFirst()
            .orElseGet(() -> usernameFactory.create(firstName, lastName, 0));
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
