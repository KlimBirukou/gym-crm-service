package com.epam.gym.validator.single.trainee;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.exception.DateValidationException;
import com.epam.gym.validator.IValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;

@Service("traineeBirthdateValidator")
@RequiredArgsConstructor
public class TraineeBirthdateValidator
    implements IValidator<LocalDate> {

    private final Clock clock;

    @Value("${gym.validation.trainee.min-age:18}")
    private final int minAge;
    @Value("${gym.validation.trainee.max-age:100}")
    private final int maxAge;

    @Override
    public void validate(@NonNull LocalDate target) {
        LocalDate today = LocalDate.now(clock);
        if (target.isAfter(today)
            || target.isAfter(today.minusYears(minAge))
            || target.isBefore(today.minusYears(maxAge))) {
            throw new DateValidationException(Trainee.class.getSimpleName());
        }
    }
}
