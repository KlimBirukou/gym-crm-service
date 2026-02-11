package com.epam.gym.service.user;

import com.epam.gym.domain.user.User;
import com.epam.gym.service.user.dto.AuthenticateDto;
import com.epam.gym.service.user.dto.ChangePasswordDto;
import com.epam.gym.service.user.dto.ToggleStatusDto;
import lombok.NonNull;

import java.util.UUID;

public interface IUserService {

    User authenticate(@NonNull AuthenticateDto dto);

    User findByUsername(@NonNull String username);

    void changePassword(@NonNull ChangePasswordDto dto);

    void updateUserData(@NonNull UUID userUid,
                        @NonNull String firstName,
                        @NonNull String lastName,
                        @NonNull String username);

    void toggleStatus(@NonNull ToggleStatusDto dto);
}
