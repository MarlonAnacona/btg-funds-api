package com.btgpactual.fondos.services;

import com.btgpactual.fondos.models.document.Customer;
import com.btgpactual.fondos.models.document.Fund;
import com.btgpactual.fondos.models.document.Transaction;
import com.btgpactual.fondos.models.document.User;
import com.btgpactual.fondos.models.dto.CancellationRequest;
import com.btgpactual.fondos.models.dto.Response;
import com.btgpactual.fondos.models.dto.SubscriptionRequest;
import com.btgpactual.fondos.models.embedded.Investement;
import com.btgpactual.fondos.models.enums.TransactionType;
import com.btgpactual.fondos.repositories.CustomerRepository;
import com.btgpactual.fondos.repositories.FundRepository;
import com.btgpactual.fondos.repositories.TransactionRespository;
import com.btgpactual.fondos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class Investment implements  IInvestment{


    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    FundRepository fundRepository;

    @Autowired
    TransactionRespository transactionRespository;
    @Autowired
    UserRepository userRepository;
    @Override
    public Response cancellation(String email, CancellationRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Customer customer = customerRepository.findById(user.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Fund fund = fundRepository.findById(request.getFundId())
                .orElseThrow(() -> new RuntimeException("Fund not found"));

        Investement investment = customer.getInvestments().stream()
                .filter(inv -> inv.getFundId().equals(fund.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No está suscrito a este fondo"));

        customer.setBalance(
                customer.getBalance().add(investment.getAmount())
        );

        customer.getInvestments().remove(investment);

        Transaction transaction = Transaction.builder()
                .amount(investment.getAmount())
                .type(TransactionType.CANCELLATION)
                .fundName(fund.getName())
                .customerId(customer.getId())
                .date(LocalDateTime.now())
                .build();

        transactionRespository.save(transaction);

        customerRepository.save(customer);

        return new Response("Cancelación exitosa");


    }

    @Override
    public Response subscribe(String email, SubscriptionRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Customer customer = customerRepository.findById(user.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Fund fund = fundRepository.findById(request.getFundId())
                .orElseThrow(() -> new RuntimeException("Fund not found"));


            if (customer.getBalance().compareTo(fund.getMinimumAmount()) < 0) {
                throw new RuntimeException(
                        "No tiene saldo disponible para vincularse al fondo " + fund.getName()
                );
            }
                Transaction transaction= new Transaction();
                transaction.setAmount(request.getAmount());
                transaction.setType(TransactionType.OPENING);
                transaction.setFundName(fund.getName());
                transaction.setCustomerId(customer.getId());
                transaction.setDate(LocalDateTime.now());
                transactionRespository.save(transaction);

                customer.setBalance(
                        customer.getBalance().subtract(request.getAmount())
                );
                customerRepository.save(customer);
                return new Response("Suscripción exitosa");

    }




    @Override
    public List<?> getAll(String email) {
        return null;
    }
}
