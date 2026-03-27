package com.btgpactual.fondos.servicestest;



import com.btgpactual.fondos.models.document.Fund;
import com.btgpactual.fondos.models.document.Transaction;
import com.btgpactual.fondos.models.document.User;
import com.btgpactual.fondos.models.dto.CancellationRequest;
import com.btgpactual.fondos.models.dto.Response;
import com.btgpactual.fondos.models.dto.SubscriptionRequest;
import com.btgpactual.fondos.models.embedded.Investment;
import com.btgpactual.fondos.models.enums.NotificationPreference;
import com.btgpactual.fondos.repositories.FundRepository;
import com.btgpactual.fondos.repositories.TransactionRespository;
import com.btgpactual.fondos.repositories.UserRepository;
import com.btgpactual.fondos.services.impl.InvestmentImpl;
import com.btgpactual.fondos.services.notification.NotificationFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestmentImpltTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FundRepository fundRepository;

    @Mock
    private TransactionRespository transactionRespository;

    @Mock
    private NotificationFactory notificationFactory;

    @InjectMocks
    private InvestmentImpl investmentService;

    @Test
    void subscribe_success() throws Exception {
        String email = "test@mail.com";

        User user = new User();
        user.setId("1");
        user.setEmail(email);
        user.setBalance(new BigDecimal("1000"));
        user.setInvestments(new ArrayList<>());
        user.setNotificationPreference(NotificationPreference.EMAIL);

        Fund fund = new Fund();
        fund.setId("F1");
        fund.setName("Fondo Test");
        fund.setMinimumAmount(new BigDecimal("100"));

        SubscriptionRequest request = new SubscriptionRequest();
        request.setFundId("F1");
        request.setAmount(new BigDecimal("200"));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(fundRepository.findById("F1")).thenReturn(Optional.of(fund));

        Response response = investmentService.subscribe(email, request);

        assertEquals("Suscripción exitosa", response.getResponse());
        assertEquals(new BigDecimal("800"), user.getBalance()); // 1000 - 200
        assertEquals(1, user.getInvestments().size());

        verify(transactionRespository).save(any(Transaction.class));
        verify(userRepository).save(user);
        verify(notificationFactory).getService(any());
    }

    @Test
    void subscribe_no_balance() {
        String email = "test@mail.com";

        User user = new User();
        user.setId("1");
        user.setEmail(email);
        user.setBalance(new BigDecimal("50"));
        user.setInvestments(new ArrayList<>());

        Fund fund = new Fund();
        fund.setId("F1");
        fund.setName("Fondo Test");
        fund.setMinimumAmount(new BigDecimal("100"));

        SubscriptionRequest request = new SubscriptionRequest();
        request.setFundId("F1");
        request.setAmount(new BigDecimal("200"));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(fundRepository.findById("F1")).thenReturn(Optional.of(fund));

        // Act & Assert
        Exception ex = assertThrows(Exception.class, () -> {
            investmentService.subscribe(email, request);
        });

        assertTrue(ex.getMessage().contains("No tiene saldo"));
    }

    @Test
    void cancellation_success() {
        String email = "test@mail.com";

        Investment investment = new Investment();
        investment.setFundId("F1");
        investment.setAmount(new BigDecimal("200"));

        User user = new User();
        user.setId("1");
        user.setEmail(email);
        user.setBalance(new BigDecimal("500"));
        user.setInvestments(new ArrayList<>(List.of(investment)));

        Fund fund = new Fund();
        fund.setId("F1");
        fund.setName("Fondo Test");

        CancellationRequest request = new CancellationRequest();
        request.setFundId("F1");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(fundRepository.findById("F1")).thenReturn(Optional.of(fund));

        Response response = investmentService.cancellation(email, request);

        assertEquals("Cancelación exitosa", response.getResponse());
        assertEquals(new BigDecimal("700"), user.getBalance()); // 500 + 200
        assertEquals(0, user.getInvestments().size());

    }

    @Test
    void cancellation_user_not_found() {
        String email = "test@mail.com";
        CancellationRequest request = new CancellationRequest();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            investmentService.cancellation(email, request);
        });

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void getAll_success() {
        String email = "test@mail.com";

        User user = new User();
        user.setId("1");

        List<Transaction> transactions = List.of(new Transaction());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(transactionRespository.findByCustomerId("1")).thenReturn(transactions);

        List<Transaction> result = investmentService.getAll(email);

        assertEquals(1, result.size());
        verify(transactionRespository).findByCustomerId("1");
    }
}