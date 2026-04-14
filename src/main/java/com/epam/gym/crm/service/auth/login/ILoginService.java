package com.epam.gym.crm.service.auth.login;

import com.epam.gym.crm.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.crm.controller.rest.auth.dto.response.LoginResponse;
import lombok.NonNull;

public interface ILoginService {

    LoginResponse login(@NonNull LoginRequest request);
}
