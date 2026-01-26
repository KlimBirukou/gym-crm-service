package com.epam.gym.service.generator.name.supplier;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Primary
@Service
public final class CompositeUsernameSupplier implements IUsernameSupplier {

    private Collection<IUsernameSupplier> usernameSuppliers;

    @Override
    public List<String> supply(@NonNull String firstName, @NonNull String lastName) {
        return usernameSuppliers.stream()
            .map(usernameSupplier -> usernameSupplier.supply(firstName, lastName))
            .flatMap(Collection::stream)
            .toList();
    }

    @Autowired
    public void setUsernameSuppliers(Collection<IUsernameSupplier> usernameSuppliers) {
        this.usernameSuppliers = usernameSuppliers;
    }
}
