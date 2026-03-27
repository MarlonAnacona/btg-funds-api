package com.btgpactual.fondos.controllertest;

import com.btgpactual.fondos.controller.InvestmentController;
import com.btgpactual.fondos.models.document.Transaction;
import com.btgpactual.fondos.models.dto.CancellationRequest;
import com.btgpactual.fondos.models.dto.Response;
import com.btgpactual.fondos.models.dto.SubscriptionRequest;
import com.btgpactual.fondos.services.IInvestment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class InvestmentControllerTest {

    @Mock
    private IInvestment investmentService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private InvestmentController investmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testSubscribe_Success() throws Exception {
        // Arrange
        String email = "test@example.com";
        SubscriptionRequest request = new SubscriptionRequest();
        Response mockResponse = new Response(); // Use the correct type
        mockResponse.setResponse("Subscription successful"); // Set mock data
        when(authentication.getName()).thenReturn(email);
        when(investmentService.subscribe(email, request)).thenReturn(mockResponse);

        // Act
        ResponseEntity<?> response = investmentController.subscribe(request, authentication);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(investmentService, times(1)).subscribe(email, request);
    }

    @Test
    void testSubscribe_Exception() throws Exception {
        // Arrange
        String email = "test@example.com";
        SubscriptionRequest request = new SubscriptionRequest();
        when(authentication.getName()).thenReturn(email);
        when(investmentService.subscribe(email, request)).thenThrow(new RuntimeException("Error subscribing"));

        // Act
        ResponseEntity<?> response = investmentController.subscribe(request, authentication);

        // Assert
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        Response errorResponse = (Response) response.getBody();
        assertEquals("Error subscribing", errorResponse.getResponse());
        verify(investmentService, times(1)).subscribe(email, request);
    }

    @Test
    void testCancellation_Success() {
        // Arrange
        String email = "test@example.com";
        CancellationRequest request = new CancellationRequest();
        Response mockResponse = new Response(); // Use the correct type
        mockResponse.setResponse("Cancellation successful"); // Set mock data
        when(authentication.getName()).thenReturn(email);
        when(investmentService.cancellation(email, request)).thenReturn(mockResponse);

        // Act
        ResponseEntity<?> response = investmentController.cancellation(request, authentication);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(investmentService, times(1)).cancellation(email, request);
    }

    @Test
    void testCancellation_Exception() {
        // Arrange
        String email = "test@example.com";
        CancellationRequest request = new CancellationRequest();
        when(authentication.getName()).thenReturn(email);
        when(investmentService.cancellation(email, request)).thenThrow(new RuntimeException("Error cancelling"));

        // Act
        ResponseEntity<?> response = investmentController.cancellation(request, authentication);

        // Assert
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        Response errorResponse = (Response) response.getBody();
        assertEquals("Error cancelling", errorResponse.getResponse());
        verify(investmentService, times(1)).cancellation(email, request);
    }

    @Test
    void testGetAllTransactions_Success() {
        // Arrange
        String email = "test@example.com";
        List<Transaction> mockTransactions = List.of(new Transaction()); // Mock data
        when(authentication.getName()).thenReturn(email);
        when(investmentService.getAll(email)).thenReturn((List<Transaction>) mockTransactions);

        // Act
        ResponseEntity<?> response = investmentController.getAllTransactions(authentication);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTransactions, response.getBody());
        verify(investmentService, times(1)).getAll(email);
    }

    @Test
    void testGetAllTransactions_Exception() {
        // Arrange
        String email = "test@example.com";
        when(authentication.getName()).thenReturn(email);
        when(investmentService.getAll(email)).thenThrow(new RuntimeException("Error fetching transactions"));

        // Act
        ResponseEntity<?> response = investmentController.getAllTransactions(authentication);

        // Assert
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        Response errorResponse = (Response) response.getBody();
        assertEquals("Error fetching transactions", errorResponse.getResponse());
        verify(investmentService, times(1)).getAll(email);
    }
}