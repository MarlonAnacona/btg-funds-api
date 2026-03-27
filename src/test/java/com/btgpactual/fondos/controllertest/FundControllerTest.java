package com.btgpactual.fondos.controllertest;

import com.btgpactual.fondos.controller.FundController;
import com.btgpactual.fondos.models.document.Fund;
import com.btgpactual.fondos.models.dto.RegisterFunds;
import com.btgpactual.fondos.models.dto.Response;
import com.btgpactual.fondos.services.IFundService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FundControllerTest {

    @Mock
    private IFundService iFundService;

    @InjectMocks
    private FundController fundController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {

        RegisterFunds request = new RegisterFunds();
        Response mockResponse = new Response("Fund registered successfully");
        when(iFundService.create(request)).thenReturn(mockResponse);


        ResponseEntity<?> response = fundController.register(request);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(iFundService, times(1)).create(request);
    }

    @Test
    void testRegister_Exception() {

        RegisterFunds request = new RegisterFunds();
        when(iFundService.create(request)).thenThrow(new RuntimeException("Error registering fund"));


        ResponseEntity<?> response = fundController.register(request);


        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        Response errorResponse = (Response) response.getBody();
        assertEquals("Error registering fund", errorResponse.getResponse());
        verify(iFundService, times(1)).create(request);
    }

    @Test
    void testGetFunds_Success() {
        List<Fund> mockFunds = List.of(new Fund());
        when(iFundService.getAll()).thenReturn(mockFunds);

        ResponseEntity<?> response = fundController.getFunds();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockFunds, response.getBody());
        verify(iFundService, times(1)).getAll();
    }

    @Test
    void testGetFunds_Exception() {

        when(iFundService.getAll()).thenThrow(new RuntimeException("Error fetching funds"));


        ResponseEntity<?> response = fundController.getFunds();


        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        Response errorResponse = (Response) response.getBody();
        assertEquals("Error fetching funds", errorResponse.getResponse());
        verify(iFundService, times(1)).getAll();
    }
}