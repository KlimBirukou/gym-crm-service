package com.epam.gym.service.auth;

import lombok.NonNull;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordService implements IPasswordService {

    @Override
    public String hashPassword(@NonNull String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    @Override
    public boolean checkPassword(@NonNull String password, @NonNull String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
