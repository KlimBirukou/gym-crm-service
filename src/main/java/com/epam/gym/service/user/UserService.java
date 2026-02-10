package com.epam.gym.service.user;

import com.epam.gym.domain.user.User;
import lombok.NonNull;

public class UserService implements IUserService {

    @Override
    public User findByUsername(@NonNull String username) {
        return null;
    }

    @Override
    public void changePassword() {

    }

    @Override
    public void updateUserData() {

    }

    @Override
    public void toggleStatus() {

    }
}
