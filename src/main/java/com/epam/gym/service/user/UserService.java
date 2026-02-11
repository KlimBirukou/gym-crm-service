package com.epam.gym.service.user;

import com.epam.gym.domain.user.User;
import com.epam.gym.exception.AuthenticationException;
import com.epam.gym.exception.DomainNotFoundException;
import com.epam.gym.repository.user.user.IUserRepository;
import com.epam.gym.service.user.dto.AuthenticateDto;
import com.epam.gym.service.user.dto.ChangePasswordDto;
import com.epam.gym.service.user.dto.ToggleStatusDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public User authenticate(@NonNull AuthenticateDto dto) {
        return userRepository.findByUsername(dto.username())
            .map(user -> validatePassword(user, dto.password()))
            .orElseThrow(() -> new DomainNotFoundException(User.class.getSimpleName(), dto.username()));
    }

    @Override
    @Transactional
    public User findByUsername(@NonNull String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new DomainNotFoundException(User.class.getSimpleName(), username));
    }

    @Override
    @Transactional
    public void changePassword(@NonNull ChangePasswordDto dto) {
        userRepository.findByUsername(dto.username())
            .map(user -> {
                validatePassword(user, dto.oldPassword());
                user.setPassword(dto.newPassword());
                return user;
            })
            .orElseThrow(() -> new DomainNotFoundException(User.class.getSimpleName(), dto.username()));
    }

    @Override
    @Transactional
    public void updateUserData(@NonNull UUID userUid,
                               @NonNull String firstName,
                               @NonNull String lastName,
                               @NonNull String username) {
        var user = userRepository.findByUid(userUid)
            .orElseThrow(() -> new DomainNotFoundException(User.class.getSimpleName(), userUid.toString()));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        if (!Objects.equals(username, user.getUsername())) {
            if (userRepository.existsByUsername(username)) {
                throw new IllegalArgumentException("This username is already taken");
            }
            user.setUsername(username);
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void toggleStatus(@NonNull ToggleStatusDto dto) {
        var user = userRepository.findByUsername(dto.username())
            .orElseThrow(() -> new DomainNotFoundException(User.class.getSimpleName(), dto.username()));
        user.setActive(!user.isActive());
        userRepository.save(user);
    }

    private User validatePassword(User user, String password) {
        if (!Objects.equals(user.getPassword(), password)) {
            throw new AuthenticationException(user.getUsername());
        }
        return user;
    }
}
