package com.epam.gym.validator.composite.trainee;

import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.validator.IValidator;
import com.epam.gym.validator.base.AbstractCompositeValidator;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service("createTraineeValidator")
@RequiredArgsConstructor
public class CreateTraineeValidator
    extends AbstractCompositeValidator<CreateTraineeDto>
    implements IValidator<CreateTraineeDto> {

    @Qualifier("traineeBirthdateValidator")
    private final IValidator<LocalDate> traineeBirthdateValidator;

    @Getter(AccessLevel.PROTECTED)
    private List<IValidator<CreateTraineeDto>> validators;

    @PostConstruct
    void setUpValidator() {
        validators = List.of(
            dto -> traineeBirthdateValidator.validate(dto.birthdate())
        );
    }
}
