package com.btgpactual.fondos.servicestest;


import com.btgpactual.fondos.models.document.User;
import com.btgpactual.fondos.models.dto.RegisterRequest;
import com.btgpactual.fondos.models.dto.Response;
import com.btgpactual.fondos.models.enums.NotificationPreference;
import com.btgpactual.fondos.models.enums.Role;
import com.btgpactual.fondos.repositories.UserRepository;
import com.btgpactual.fondos.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void create_success() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@mail.com");
        request.setPassword("123456");
        request.setName("Marlon");
        request.setNotificationPreference(NotificationPreference.EMAIL);

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("encoded123");

        Response response = userService.create(request);

        assertNotNull(response);
        assertEquals("Usuario creado exitosamente", response.getResponse());

        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("123456");
    }

    @Test
    void create_email_already_exists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@mail.com");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(true);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> userService.create(request)
        );

        assertEquals("Email already in use", ex.getMessage());

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void create_validate_user_fields() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@mail.com");
        request.setPassword("123456");
        request.setName("Marlon");
        request.setNotificationPreference(NotificationPreference.SMS);
        request.setRole(Role.CLIENT);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded123");

        userService.create(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();

        assertEquals("test@mail.com", saved.getEmail());
        assertEquals("encoded123", saved.getPassword());
        assertEquals("Marlon", saved.getName());
        assertEquals(Role.CLIENT, saved.getRole());
        assertEquals(0, saved.getBalance().compareTo(new BigDecimal("500000")));
        assertEquals(NotificationPreference.SMS, saved.getNotificationPreference());
        assertNotNull(saved.getInvestments());
    }

    @Test
    void get_success() {
        User user = new User();
        user.setEmail("user@test.com");

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.get();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("user@test.com", result.get(0).getEmail());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void get_empty() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<User> result = userService.get();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository, times(1)).findAll();
    }
}