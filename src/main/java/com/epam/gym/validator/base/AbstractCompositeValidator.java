package com.epam.gym.validator.base;

import com.epam.gym.validator.IValidator;
import lombok.NonNull;

import java.util.List;

public abstract class AbstractCompositeValidator<T>
    implements IValidator<T> {

    @Override
    public void validate(@NonNull T target) {
        getValidators().forEach(validator -> validator.validate(target));
    }

    protected abstract List<IValidator<T>> getValidators();
}
