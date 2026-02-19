package com.epam.gym.repository.mapper;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.repository.entity.TraineeEntity;
import com.epam.gym.repository.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ITraineeEntityToTraineeMapperTest {

    private static final LocalDate BIRTHDATE = LocalDate.of(2000, 1, 1);
    private static final UUID TRAINEE_UID = UUID.randomUUID();
    private static final UUID USER_UID = UUID.randomUUID();
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String ADDRESS = "address";

    private ITraineeEntityToTraineeMapper  testObject;

    @BeforeEach
    void setUp() {
        testObject = Mappers.getMapper(ITraineeEntityToTraineeMapper .class);
    }

    private static Stream<Arguments> provideConvertData() {
        return Stream.of(
            Arguments.of(true, ADDRESS, BIRTHDATE),
            Arguments.of(false, ADDRESS, BIRTHDATE),
            Arguments.of(true, null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertData")
    void convert_shouldMapEntityToDomain(boolean active, String address, LocalDate birthdate) {
        var userEntity = UserEntity.builder()
            .uid(USER_UID)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .username(USERNAME)
            .password(PASSWORD)
            .active(active)
            .build();
        var traineeEntity = TraineeEntity.builder()
            .uid(TRAINEE_UID)
            .user(userEntity)
            .address(address)
            .birthdate(birthdate)
            .build();

        var result = testObject.convert(traineeEntity);

        assertNotNull(result);
        assertEquals(TRAINEE_UID, result.getUid());
        assertEquals(FIRSTNAME, result.getFirstName());
        assertEquals(LASTNAME, result.getLastName());
        assertEquals(USERNAME, result.getUsername());
        assertEquals(PASSWORD, result.getPassword());
        assertEquals(active, result.isActive());
        assertEquals(address, result.getAddress());
        assertEquals(birthdate, result.getBirthdate());
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenArgumentNull(TraineeEntity entity) {
        var result = testObject.convert(entity);

        assertNull(result);
    }


    private static Stream<Arguments> provideInvertConvertData() {
        return Stream.of(
            Arguments.of(true, ADDRESS, BIRTHDATE),
            Arguments.of(false, ADDRESS, BIRTHDATE),
            Arguments.of(true, null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvertConvertData")
    void invertConvert_shouldMapDomainToEntity(boolean active, String address, LocalDate birthdate) {
        var trainee = Trainee.builder()
            .uid(TRAINEE_UID)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .username(USERNAME)
            .password(PASSWORD)
            .active(active)
            .address(address)
            .birthdate(birthdate)
            .build();

        var result = testObject.invertConvert(trainee);

        assertNotNull(result);
        assertEquals(TRAINEE_UID, result.getUid());
        assertNotNull(result.getUser());
        assertNull(result.getUser().getUid());
        assertEquals(FIRSTNAME, result.getUser().getFirstName());
        assertEquals(LASTNAME, result.getUser().getLastName());
        assertEquals(USERNAME, result.getUser().getUsername());
        assertEquals(PASSWORD, result.getUser().getPassword());
        assertEquals(active, result.getUser().isActive());
        assertEquals(address, result.getAddress());
        assertEquals(birthdate, result.getBirthdate());
    }

    @ParameterizedTest
    @NullSource
    void invertConvert_shouldReturnNull_whenArgumentNull(Trainee trainee) {
        var result = testObject.invertConvert(trainee);

        assertNull(result);
    }


    private static Stream<Arguments> provideRoundtripEntityData() {
        return Stream.of(
            Arguments.of(true, ADDRESS, BIRTHDATE),
            Arguments.of(false, null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideRoundtripEntityData")
    void roundtrip_shouldPreserveData_whenEntityToDomainToEntity(boolean active, String address, LocalDate birthdate) {
        var userEntity = UserEntity.builder()
            .uid(USER_UID)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .username(USERNAME)
            .password(PASSWORD)
            .active(active)
            .build();
        var originalEntity = TraineeEntity.builder()
            .uid(TRAINEE_UID)
            .user(userEntity)
            .address(address)
            .birthdate(birthdate)
            .build();

        var domain = testObject.convert(originalEntity);
        assertNotNull(domain);
        var resultEntity = testObject.invertConvert(domain);

        assertEquals(originalEntity.getUid(), resultEntity.getUid());
        assertEquals(originalEntity.getAddress(), resultEntity.getAddress());
        assertEquals(originalEntity.getBirthdate(), resultEntity.getBirthdate());
        assertEquals(originalEntity.getUser().getFirstName(), resultEntity.getUser().getFirstName());
        assertEquals(originalEntity.getUser().getLastName(), resultEntity.getUser().getLastName());
        assertEquals(originalEntity.getUser().getUsername(), resultEntity.getUser().getUsername());
        assertEquals(originalEntity.getUser().getPassword(), resultEntity.getUser().getPassword());
        assertEquals(originalEntity.getUser().isActive(), resultEntity.getUser().isActive());
    }

    private static Stream<Arguments> provideRoundtripDomainData() {
        return Stream.of(
            Arguments.of(true, ADDRESS, BIRTHDATE),
            Arguments.of(false, null, null)
        );
    }


    @ParameterizedTest
    @MethodSource("provideRoundtripDomainData")
    void roundtrip_shouldPreserveData_whenDomainToEntityToDomain(boolean active, String address, LocalDate birthdate) {
        var originalDomain = Trainee.builder()
            .uid(TRAINEE_UID)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .username(USERNAME)
            .password(PASSWORD)
            .active(active)
            .address(address)
            .birthdate(birthdate)
            .build();

        var entity = testObject.invertConvert(originalDomain);
        var resultDomain = testObject.convert(entity);

        assertNotNull(resultDomain);
        assertEquals(originalDomain.getUid(), resultDomain.getUid());
        assertEquals(originalDomain.getFirstName(), resultDomain.getFirstName());
        assertEquals(originalDomain.getLastName(), resultDomain.getLastName());
        assertEquals(originalDomain.getUsername(), resultDomain.getUsername());
        assertEquals(originalDomain.getPassword(), resultDomain.getPassword());
        assertEquals(originalDomain.isActive(), resultDomain.isActive());
        assertEquals(originalDomain.getAddress(), resultDomain.getAddress());
        assertEquals(originalDomain.getBirthdate(), resultDomain.getBirthdate());
    }
}
