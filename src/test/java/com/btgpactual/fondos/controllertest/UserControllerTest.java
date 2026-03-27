package com.btgpactual.fondos.controllertest;


import com.btgpactual.fondos.controller.UserController;
import com.btgpactual.fondos.models.document.User;
import com.btgpactual.fondos.models.dto.RegisterRequest;
import com.btgpactual.fondos.models.dto.Response;
import com.btgpactual.fondos.services.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private IUserService iUserService;

    @InjectMocks
    private UserController userController;

    // ---------------------- REGISTER SUCCESS ----------------------
    @Test
    void register_success() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        Response mockResponse = new Response();
        mockResponse.setResponse("Usuario creado");

        when(iUserService.create(request)).thenReturn(mockResponse);

        // Act
        ResponseEntity<?> responseEntity = userController.register(request);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());

        Response body = (Response) responseEntity.getBody();
        assertEquals("Usuario creado", body.getResponse());

        verify(iUserService, times(1)).create(request);
    }

    // ---------------------- REGISTER ERROR ----------------------
    @Test
    void register_exception() {
        // Arrange
        RegisterRequest request = new RegisterRequest();

        when(iUserService.create(request))
                .thenThrow(new RuntimeException("Error al crear usuario"));

        // Act
        ResponseEntity<?> responseEntity = userController.register(request);

        // Assert
        assertEquals(406, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());

        Response body = (Response) responseEntity.getBody();
        assertEquals("Error al crear usuario", body.getResponse());

        verify(iUserService, times(1)).create(request);
    }

    // ---------------------- GET ALL SUCCESS ----------------------
    @Test
    void getAll_success() {
        // Arrange
        List<User> mockList = new ArrayList<>();

        User user = new User();
        user.setId("1");
        mockList.add(user);

        when(iUserService.get()).thenReturn(mockList);

        // Act
        ResponseEntity<?> responseEntity = userController.getAll();

        // Assert
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());

        List<?> body = (List<?>) responseEntity.getBody();
        assertEquals(1, body.size());

        verify(iUserService, times(1)).get();
    }

    // ---------------------- GET ALL ERROR ----------------------
    @Test
    void getAll_exception() {
        // Arrange
        when(iUserService.get())
                .thenThrow(new RuntimeException("Error al obtener usuarios"));

        // Act
        ResponseEntity<?> responseEntity = userController.getAll();

        // Assert
        assertEquals(406, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());

        Response body = (Response) responseEntity.getBody();
        assertEquals("Error al obtener usuarios", body.getResponse());

        verify(iUserService, times(1)).get();
    }
}