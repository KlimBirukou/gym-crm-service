package com.epam.gym.validator;

import lombok.NonNull;

@FunctionalInterface
public interface IValidator<T> {

    void validate(@NonNull T target);
}
