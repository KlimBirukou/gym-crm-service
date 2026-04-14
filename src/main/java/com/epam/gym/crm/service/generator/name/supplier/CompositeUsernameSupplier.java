package com.epam.gym.crm.service.generator.name.supplier;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Primary
@Service
@RequiredArgsConstructor
public class CompositeUsernameSupplier implements IUsernameSupplier {

    private final Collection<IUsernameSupplier> usernameSuppliers;

    @Override
    public List<String> supply(@NonNull String firstName, @NonNull String lastName) {
        return usernameSuppliers.stream()
            .map(usernameSupplier -> usernameSupplier.supply(firstName, lastName))
            .flatMap(Collection::stream)
            .toList();
    }
}
