package com.btgpactual.fondos.servicestest;


import com.btgpactual.fondos.models.document.Fund;
import com.btgpactual.fondos.models.dto.RegisterFunds;
import com.btgpactual.fondos.models.dto.Response;
import com.btgpactual.fondos.models.enums.FundCategory;
import com.btgpactual.fondos.repositories.FundRepository;
import com.btgpactual.fondos.services.impl.FundService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FundServiceTest {

    @Mock
    private FundRepository fundRepository;

    @InjectMocks
    private FundService fundService;

    @Test
    void create_success() {
        RegisterFunds request = new RegisterFunds();
        request.setName("Fondo Test");
        request.setCategory(FundCategory.FPV);
        request.setMinimumAmount(new BigDecimal("100"));

        Response response = fundService.create(request);

        assertNotNull(response);
        assertEquals("Fondo creado exitosamente", response.getResponse());

        verify(fundRepository, times(1)).save(any(Fund.class));
    }

    @Test
    void create_request_null() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> fundService.create(null)
        );

        assertEquals("La solicitud no puede ser nula", ex.getMessage());

        verify(fundRepository, never()).save(any());
    }

    @Test
    void create_name_empty() {
        RegisterFunds request = new RegisterFunds();
        request.setName("   "); // vacío con espacios
        request.setMinimumAmount(new BigDecimal("100"));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> fundService.create(request)
        );

        assertEquals("El nombre es obligatorio", ex.getMessage());

        verify(fundRepository, never()).save(any());
    }

    @Test
    void create_amount_null() {
        RegisterFunds request = new RegisterFunds();
        request.setName("Fondo Test");
        request.setMinimumAmount(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> fundService.create(request)
        );

        assertEquals("El monto mínimo debe ser mayor a 0", ex.getMessage());

        verify(fundRepository, never()).save(any());
    }

    @Test
    void create_amount_zero() {
        RegisterFunds request = new RegisterFunds();
        request.setName("Fondo Test");
        request.setMinimumAmount(BigDecimal.ZERO);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> fundService.create(request)
        );

        assertEquals("El monto mínimo debe ser mayor a 0", ex.getMessage());

        verify(fundRepository, never()).save(any());
    }

    @Test
    void getAll_success() {
        Fund fund = new Fund();
        fund.setName("Fondo 1");

        when(fundRepository.findAll()).thenReturn(List.of(fund));

        List<Fund> result = fundService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Fondo 1", result.get(0).getName());

        verify(fundRepository, times(1)).findAll();
    }

    @Test
    void getAll_empty() {
        when(fundRepository.findAll()).thenReturn(List.of());

        List<Fund> result = fundService.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(fundRepository, times(1)).findAll();
    }

    @Test
    void getAll_exception() {
        when(fundRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> fundService.getAll()
        );

        assertTrue(ex.getMessage().contains("Error al obtener los fondos"));

        verify(fundRepository, times(1)).findAll();
    }
}