package com.epam.gym.repository.domain.user;

import com.epam.gym.domain.user.User;
import com.epam.gym.exception.not.found.UserNotFoundException;
import com.epam.gym.repository.entity.UserEntity;
import com.epam.gym.repository.jpa.user.IUserEntityRepository;
import com.epam.gym.repository.mapper.IUserEntityToUserMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class JpaUserRepositoryTest {

    private static final String USERNAME = "username";
    private static final User USER = User.builder().username(USERNAME).build();
    private static final UserEntity USER_ENTITY = new UserEntity();

    @Mock
    private IUserEntityRepository repository;
    @Mock
    private ConversionService conversionService;
    @Mock
    private IUserEntityToUserMapper mapper;

    @InjectMocks
    private JpaUserRepository testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(repository, conversionService, mapper);
    }

    @Test
    void getByUsername_shouldReturnUser_whenFound() {
        doReturn(Optional.of(USER_ENTITY)).when(repository).findByUsername(USERNAME);
        doReturn(USER).when(conversionService).convert(USER_ENTITY, User.class);

        var result = testObject.getByUsername(USERNAME);

        assertTrue(result.isPresent());
        assertEquals(USER, result.get());
        verify(repository).findByUsername(USERNAME);
    }

    @Test
    void getByUsername_shouldReturnEmpty_whenNotFound() {
        doReturn(Optional.empty()).when(repository).findByUsername(USERNAME);

        var result = testObject.getByUsername(USERNAME);

        assertTrue(result.isEmpty());
        verifyNoInteractions(conversionService);
    }

    @ParameterizedTest
    @NullSource
    void getByUsername_shouldThrowException_whenArgumentNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.getByUsername(username));
    }


    @Test
    void save_shouldSaveEntity() {
        doReturn(USER_ENTITY).when(conversionService).convert(USER, UserEntity.class);

        testObject.save(USER);

        verify(repository).save(USER_ENTITY);
    }

    @ParameterizedTest
    @NullSource
    void save_shouldThrowException_whenArgumentNull(User user) {
        assertThrows(NullPointerException.class, () -> testObject.save(user));
    }


    @Test
    void update_shouldUpdateAndSave_whenUserExists() {
        doReturn(Optional.of(USER_ENTITY)).when(repository).findByUsername(USERNAME);

        testObject.update(USER);

        verify(mapper).updateEntity(USER, USER_ENTITY);
        verify(repository).save(USER_ENTITY);
    }

    @Test
    void update_shouldThrowException_whenUserNotFound() {
        doReturn(Optional.empty()).when(repository).findByUsername(USERNAME);

        assertThrows(UserNotFoundException.class, () -> testObject.update(USER));

        verify(repository).findByUsername(USERNAME);
        verifyNoInteractions(mapper);
    }

    @ParameterizedTest
    @NullSource
    void update_shouldThrowException_whenArgumentNull(User user) {
        assertThrows(NullPointerException.class, () -> testObject.update(user));
    }
}
