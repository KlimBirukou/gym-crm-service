package com.epam.gym.crm.service.generator.name;

import com.epam.gym.crm.configuration.properties.UserProperties;
import com.epam.gym.crm.service.generator.name.factory.IUsernameFactory;
import com.epam.gym.crm.service.generator.name.supplier.IUsernameSupplier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class DefaultUsernameGenerator implements IUsernameGenerator {

    public static final String MESSAGE = "firstName or lastName cannot be blank";

    private final IUsernameFactory usernameFactory;
    private final IUsernameSupplier usernameSupplier;
    private final UserProperties userProperties;

    @Override
    public String generate(@NonNull String firstName, @NonNull String lastName) {
        validateInput(firstName, lastName);
        var usernames = usernameSupplier.supply(firstName, lastName);
        if (usernames.isEmpty()) {
            return usernameFactory.create(firstName, lastName);
        }
        return usernames.stream()
            .map(username -> username.split(Pattern.quote(userProperties.usernameDelimiter())))
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
            throw new IllegalArgumentException(MESSAGE);
        }
    }
}
