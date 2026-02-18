package com.epam.gym.service.auth;

import lombok.NonNull;
import org.springframework.stereotype.Service;

import static org.springframework.security.crypto.bcrypt.BCrypt.checkpw;
import static org.springframework.security.crypto.bcrypt.BCrypt.gensalt;
import static org.springframework.security.crypto.bcrypt.BCrypt.hashpw;

@Service
public class PasswordService implements IPasswordService {

    @Override
    public String hashPassword(@NonNull String password) {
        return hashpw(password, gensalt(12));
    }

    @Override
    public boolean checkPassword(@NonNull String password, @NonNull String hashedPassword) {
        return checkpw(password, hashedPassword);
    }
}
