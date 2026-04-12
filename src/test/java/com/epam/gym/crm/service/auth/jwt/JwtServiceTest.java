package com.epam.gym.crm.service.auth.jwt;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.SecureRandom;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private static final String USERNAME = "username";
    public static final long JWT_EXPIRATION = 3600L;
    public static final String TOKEN = "this-is-not-a-jwt";

    @Mock
    private AuthProperties authProperties;

    @InjectMocks
    private JwtService testObject;

    @Test
    void generateToken_shouldCreateValidToken() {
        var secret = generateBase64Secret();
        doReturn(secret).when(authProperties).secret();
        doReturn(JWT_EXPIRATION).when(authProperties).jwtExpiration();

        var token = testObject.generateToken(USERNAME);

        assertNotNull(token);
        verify(authProperties).jwtExpiration();
        verify(authProperties).secret();
    }

    @Test
    void extractUsername_shouldReturnUsername_forGeneratedToken() {
        var secret = generateBase64Secret();
        doReturn(secret).when(authProperties).secret();
        doReturn(JWT_EXPIRATION).when(authProperties).jwtExpiration();

        var token = testObject.generateToken(USERNAME);

        var extracted = testObject.extractUsername(token);

        assertEquals(USERNAME, extracted);
        verify(authProperties).jwtExpiration();
        verify(authProperties, times(2)).secret();
    }

    @Test
    void isTokenValid_shouldReturnTrue_forGeneratedToken() {
        var secret = generateBase64Secret();
        doReturn(secret).when(authProperties).secret();
        doReturn(JWT_EXPIRATION).when(authProperties).jwtExpiration();

        var token = testObject.generateToken(USERNAME);

        var valid = testObject.isTokenValid(token);

        assertTrue(valid);
       verify(authProperties).jwtExpiration();
        verify(authProperties, times(2)).secret();
    }

    @Test
    void isTokenValid_shouldReturnFalse_forInvalidToken() {
        var secret = generateBase64Secret();
        doReturn(secret).when(authProperties).secret();

        var valid = testObject.isTokenValid(TOKEN);

        assertFalse(valid);
        verify(authProperties).secret();
    }

    @Test
    void extractUsername_shouldThrow_whenTokenInvalid() {
        var secret = generateBase64Secret();
        doReturn(secret).when(authProperties).secret();

        assertThrows(JwtException.class, () -> testObject.extractUsername(TOKEN));
        verify(authProperties).secret();
    }

    @ParameterizedTest
    @NullSource
    void generateToken_shouldThrowNPE_whenUsernameNull(String token) {
        assertThrows(NullPointerException.class, () -> testObject.generateToken(token));
    }

    @ParameterizedTest
    @NullSource
    void extractUsername_shouldThrowNPE_whenTokenNull(String token) {
        assertThrows(NullPointerException.class, () -> testObject.extractUsername(token));
    }

    @ParameterizedTest
    @NullSource
    void isTokenValid_shouldThrowNPE_whenTokenNull(String token) {
        assertThrows(NullPointerException.class, () -> testObject.isTokenValid(token));
    }

    private static String generateBase64Secret() {
        byte[] key = new byte[64];
        new SecureRandom().nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}
