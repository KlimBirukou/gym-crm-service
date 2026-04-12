package com.epam.gym.crm.service.user;

import com.epam.gym.crm.domain.user.User;
import com.epam.gym.crm.service.user.dto.ChangePasswordDto;
import lombok.NonNull;

public interface IUserService {

    User getByUsername(@NonNull String username);

    void changePassword(@NonNull ChangePasswordDto dto);

    void changeStatus(@NonNull String username, @NonNull Boolean status);
}
