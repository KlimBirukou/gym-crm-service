package com.epam.gym.crm.service.user;

import com.epam.gym.crm.domain.user.User;
import com.epam.gym.crm.exception.auth.InvalidCredentialsException;
import com.epam.gym.crm.exception.not.found.UserNotFoundException;
import com.epam.gym.crm.repository.domain.user.IUserRepository;
import com.epam.gym.crm.service.auth.password.IPasswordService;
import com.epam.gym.crm.service.user.dto.ChangePasswordDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final UUID UID = UUID.randomUUID();
    private static final String USERNAME = "username";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String OLD_PASSWORD = "old_password";
    private static final String OLD_HASHED_PASSWORD = "old_hashed_password";
    private static final String NEW_PASSWORD = "new_password";
    private static final String NEW_HASHED_PASSWORD = "new_hashed_password";

    @Mock
    private IUserRepository userRepository;
    @Mock
    private IPasswordService passwordService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @InjectMocks
    private UserService testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(userRepository, passwordService);
    }

    @Test
    void getByUsername_shouldReturnUser_whenUserExist() {
        var user = buildUser();
        doReturn(Optional.of(user)).when(userRepository).getByUsername(USERNAME);

        var result = testObject.getByUsername(USERNAME);

        assertSame(user, result);
    }

    @Test
    void getByUsername_shouldThrowException_whenUserNotExist() {
        doReturn(Optional.empty()).when(userRepository).getByUsername(USERNAME);

        assertThrows(UserNotFoundException.class, () -> testObject.getByUsername(USERNAME));
    }

    @ParameterizedTest
    @NullSource
    void getByUsername_shouldThrowException_whenArgumentNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.getByUsername(username));
    }

    @Test
    void changePassword_shouldChangePassword_whenUserExistAndPasswordValid() {
        var user = buildUser();
        var dto = buildChangePasswordDto();
        doReturn(Optional.of(user)).when(userRepository).getByUsername(USERNAME);
        doReturn(true).when(passwordService).checkPassword(OLD_PASSWORD, OLD_HASHED_PASSWORD);
        doReturn(NEW_HASHED_PASSWORD).when(passwordService).hashPassword(NEW_PASSWORD);

        testObject.changePassword(dto);

        verify(userRepository).update(userCaptor.capture());
        var updated = userCaptor.getValue();

        assertEquals(NEW_HASHED_PASSWORD, updated.getPassword());
        assertEquals(USERNAME, updated.getUsername());
    }

    @Test
    void changePassword_shouldThrowException_whenUserNotExist() {
        var dto = buildChangePasswordDto();
        doReturn(Optional.empty()).when(userRepository).getByUsername(USERNAME);

        assertThrows(UserNotFoundException.class, () -> testObject.changePassword(dto));
    }

    @Test
    void changePassword_shouldThrowException_whenUserExistAndPasswordInvalid() {
        var user = buildUser();
        var dto = buildChangePasswordDto();
        doReturn(Optional.of(user)).when(userRepository).getByUsername(USERNAME);
        doReturn(false).when(passwordService).checkPassword(OLD_PASSWORD, OLD_HASHED_PASSWORD);

        assertThrows(InvalidCredentialsException.class, () -> testObject.changePassword(dto));
    }

    @ParameterizedTest
    @NullSource
    void changePassword_shouldThrowException_whenArgumentNull(ChangePasswordDto dto) {
        assertThrows(NullPointerException.class, () -> testObject.changePassword(dto));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeStatus_shouldChange_whenUserExist(boolean newStatus) {
        var user = buildUser();
        doReturn(Optional.of(user)).when(userRepository).getByUsername(USERNAME);

        testObject.changeStatus(USERNAME, newStatus);

        verify(userRepository).update(userCaptor.capture());
        assertEquals(newStatus, userCaptor.getValue().isActive());
    }

    @Test
    void changeStatus_shouldThrowException_whenUserNotExist() {
        doReturn(Optional.empty()).when(userRepository).getByUsername(USERNAME);

        assertThrows(UserNotFoundException.class, () -> testObject.changeStatus(USERNAME, true));
    }

    private static Stream<Arguments> changeStatusProvideNullArguments() {
        return Stream.of(
            Arguments.of(null, true),
            Arguments.of(USERNAME, null)
        );
    }

    @ParameterizedTest
    @MethodSource("changeStatusProvideNullArguments")
    void changeStatus_shouldThrowNpe_whenArgumentsAreNull(String username, Boolean status) {
        assertThrows(NullPointerException.class, () -> testObject.changeStatus(username, status));
    }

    private static User buildUser() {
        return User.builder()
            .uid(UID)
            .username(USERNAME)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .password(OLD_HASHED_PASSWORD)
            .active(true)
            .build();
    }

    private static ChangePasswordDto buildChangePasswordDto() {
        return ChangePasswordDto.builder()
            .username(USERNAME)
            .oldPassword(OLD_PASSWORD)
            .newPassword(NEW_PASSWORD)
            .build();
    }
}
