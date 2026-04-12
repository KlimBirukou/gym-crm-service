package com.epam.gym.crm.repository.mapper;

import com.epam.gym.crm.domain.user.User;
import com.epam.gym.crm.repository.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class IUserEntityToUserMapperTest {

    private static final UUID UID = UUID.randomUUID();
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private final IUserEntityToUserMapper testObject
        = Mappers.getMapper(IUserEntityToUserMapper.class);

    private static Stream<Boolean> provideConvertData() {
        return Stream.of(true, false);
    }

    @ParameterizedTest
    @MethodSource("provideConvertData")
    void convert_shouldMapEntityToDomain(boolean active) {
        var entity = UserEntity.builder()
            .uid(UID)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .username(USERNAME)
            .password(PASSWORD)
            .active(active)
            .build();

        var result = testObject.convert(entity);

        assertNotNull(result);
        assertEquals(UID, result.getUid());
        assertEquals(FIRSTNAME, result.getFirstName());
        assertEquals(LASTNAME, result.getLastName());
        assertEquals(USERNAME, result.getUsername());
        assertEquals(PASSWORD, result.getPassword());
        assertEquals(active, result.isActive());
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenArgumentNull(UserEntity entity) {
        var result = testObject.convert(entity);

        assertNull(result);
    }

    private static Stream<Arguments> provideConvertBackData() {
        return Stream.of(
            Arguments.of(true),
            Arguments.of(false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertBackData")
    void convert_shouldMapDomainToEntity(boolean active) {
        var user = User.builder()
            .uid(UID)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .username(USERNAME)
            .password(PASSWORD)
            .active(active)
            .build();

        var result = testObject.convert(user);

        assertNotNull(result);
        assertEquals(UID, result.getUid());
        assertEquals(FIRSTNAME, result.getFirstName());
        assertEquals(LASTNAME, result.getLastName());
        assertEquals(USERNAME, result.getUsername());
        assertEquals(PASSWORD, result.getPassword());
        assertEquals(active, result.isActive());
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenDomainArgumentNull(User user) {
        var result = testObject.convert(user);

        assertNull(result);
    }

    private static Stream<Arguments> provideUpdateEntityData() {
        return Stream.of(
            Arguments.of(FIRSTNAME, LASTNAME, PASSWORD, true),
            Arguments.of("NewFirst", "NewLast", "NewPass", false),
            Arguments.of("Updated", "Name", "Pass123", true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideUpdateEntityData")
    void updateEntity_shouldUpdateEntityFields(String firstName, String lastName, String password, boolean active) {
        var existingEntity = UserEntity.builder()
            .uid(UID)
            .firstName("OldFirst")
            .lastName("OldLast")
            .username(USERNAME)
            .password("OldPassword")
            .active(false)
            .build();

        var updatedDomain = User.builder()
            .uid(UUID.randomUUID())  // Should be ignored
            .firstName(firstName)
            .lastName(lastName)
            .username("NewUsername")  // Should be ignored
            .password(password)
            .active(active)
            .build();

        testObject.updateEntity(updatedDomain, existingEntity);

        assertEquals(UID, existingEntity.getUid());  // UID not changed
        assertEquals(firstName, existingEntity.getFirstName());
        assertEquals(lastName, existingEntity.getLastName());
        assertEquals(USERNAME, existingEntity.getUsername());  // Username not changed
        assertEquals(password, existingEntity.getPassword());
        assertEquals(active, existingEntity.isActive());
    }

    @Test
    void updateEntity_shouldPreserveUidAndUsername() {
        var existingEntity = UserEntity.builder()
            .uid(UID)
            .firstName("Old")
            .lastName("Name")
            .username(USERNAME)
            .password("old")
            .active(false)
            .build();

        var updatedDomain = User.builder()
            .uid(UUID.randomUUID())
            .firstName("New")
            .lastName("Updated")
            .username("DifferentUsername")
            .password("new")
            .active(true)
            .build();

        testObject.updateEntity(updatedDomain, existingEntity);

        assertEquals(UID, existingEntity.getUid());
        assertEquals(USERNAME, existingEntity.getUsername());
    }
}
