package com.epam.gym.crm.service.auth.password;

import lombok.NonNull;

public interface IPasswordService {

    String hashPassword(@NonNull String password);

    boolean checkPassword(@NonNull String password, @NonNull String hashedPassword);
}
