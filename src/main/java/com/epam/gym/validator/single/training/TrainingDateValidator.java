package com.epam.gym.validator.single.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.exception.DateValidationException;
import com.epam.gym.validator.IValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;

/*@Service("trainingDateValidator")
@RequiredArgsConstructor
public class TrainingDateValidator
    implements IValidator<LocalDate> {

    private final Clock clock;

    @Value("${gym.validation.training.min-days:1}")
    private final int minDays;
    @Value("${gym.validation.training.max-months:3}")
    private final int maxMonths;

    @Override
    public void validate(@NonNull LocalDate target) {
        LocalDate today = LocalDate.now(clock);
        if (target.isBefore(today)
            || target.isBefore(today.plusDays(minDays))
            || target.isAfter(today.plusMonths(maxMonths))) {
            throw new DateValidationException(Training.class.getSimpleName());
        }
    }
}*/
