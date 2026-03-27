package com.btgpactual.fondos.controllertest;


import com.btgpactual.fondos.controller.AuthController;
import com.btgpactual.fondos.models.document.User;
import com.btgpactual.fondos.models.dto.AuthRequest;
import com.btgpactual.fondos.models.dto.AuthResponse;
import com.btgpactual.fondos.models.enums.Role;
import com.btgpactual.fondos.repositories.UserRepository;
import com.btgpactual.fondos.security.JwtProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthController authController;

    // ---------------------- LOGIN SUCCESS ----------------------
    @Test
    void login_success() {
        // Arrange
        AuthRequest request = new AuthRequest();
        request.setEmail("test@mail.com");
        request.setPassword("123456");

        User user = new User();
        user.setEmail("test@mail.com");
        user.setRole(Role.ADMIN);

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        when(jwtProvider.generateToken("test@mail.com", "ADMIN"))
                .thenReturn("fake-jwt-token");

        // Act
        AuthResponse response = authController.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail("test@mail.com");
        verify(jwtProvider).generateToken("test@mail.com", "ADMIN");
    }

    // ---------------------- USER NOT FOUND ----------------------
    @Test
    void login_user_not_found() {
        // Arrange
        AuthRequest request = new AuthRequest();
        request.setEmail("noexist@mail.com");
        request.setPassword("123456");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(userRepository.findByEmail("noexist@mail.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            authController.login(request);
        });

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail("noexist@mail.com");
    }
}