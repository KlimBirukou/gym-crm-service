package com.epam.gym.service.auth.login;

import com.epam.gym.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.controller.rest.auth.dto.response.LoginResponse;
import lombok.NonNull;

public interface ILoginService {

    LoginResponse login(@NonNull LoginRequest request);
}
