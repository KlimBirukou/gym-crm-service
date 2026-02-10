package com.epam.gym.service.user;

import com.epam.gym.domain.user.User;
import lombok.NonNull;

public interface IUserService {

    User findByUsername(@NonNull String username);

     void changePassword();

     void updateUserData();

     void toggleStatus();
}
