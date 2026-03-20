package com.epam.gym.service.user;

import com.epam.gym.domain.user.User;
import com.epam.gym.exception.auth.InvalidCredentialsException;
import com.epam.gym.exception.not.found.UserNotFoundException;
import com.epam.gym.repository.domain.user.IUserRepository;
import com.epam.gym.service.auth.IPasswordService;
import com.epam.gym.service.user.dto.ChangePasswordDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IPasswordService passwordService;

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(@NonNull String username) {
        return userRepository.getByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    @Transactional
    public void changePassword(@NonNull ChangePasswordDto dto) {
        var user = userRepository.getByUsername(dto.username())
            .orElseThrow(() -> new UserNotFoundException(dto.username()));
        Optional.of(dto)
            .filter(d -> passwordService.checkPassword(d.oldPassword(), user.getPassword()))
            .orElseThrow(InvalidCredentialsException::new);
        user.setPassword(passwordService.hashPassword(dto.newPassword()));
        userRepository.update(user);
    }

    @Override
    @Transactional
    public void changeStatus(@NonNull String username, @NonNull Boolean status) {
        var user = userRepository.getByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(username));
        user.setActive(status);
        userRepository.update(user);
    }
}
