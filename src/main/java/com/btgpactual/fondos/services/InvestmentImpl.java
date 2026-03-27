package com.btgpactual.fondos.services;

import com.btgpactual.fondos.models.document.Fund;
import com.btgpactual.fondos.models.document.Transaction;
import com.btgpactual.fondos.models.document.User;
import com.btgpactual.fondos.models.dto.CancellationRequest;
import com.btgpactual.fondos.models.dto.Response;
import com.btgpactual.fondos.models.dto.SubscriptionRequest;
import com.btgpactual.fondos.models.embedded.Investment;
import com.btgpactual.fondos.models.enums.TransactionType;
import com.btgpactual.fondos.repositories.FundRepository;
import com.btgpactual.fondos.repositories.TransactionRespository;
import com.btgpactual.fondos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InvestmentImpl implements  IInvestment{


    @Autowired
    UserRepository userRepository;
    @Autowired
    FundRepository fundRepository;

    @Autowired
    TransactionRespository transactionRespository;
   
    @Override
    public Response cancellation( String email, CancellationRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Fund fund = fundRepository.findById(request.getFundId())
                .orElseThrow(() -> new RuntimeException("Fund not found"));

      Investment investment = user.getInvestments().stream()
                .filter(inv -> inv.getFundId().equals(fund.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No está suscrito a este fondo"));

        user.setBalance(
                user.getBalance().add(investment.getAmount())
        );

        user.getInvestments().remove(investment);

        Transaction transaction = getTransaction(user, fund, investment);

        transactionRespository.save(transaction);

        userRepository.save(user);

        return new Response("Cancelación exitosa");


    }

    private static Transaction getTransaction(User user, Fund fund, Investment investment) {
        Transaction transaction = Transaction.builder()
                .amount(investment.getAmount())
                .type(TransactionType.CANCELLATION)
                .fundName(fund.getName())
                .customerId(user.getId())
                .date(LocalDateTime.now())
                .fundId(fund.getId())
                .build();
        return transaction;
    }

    @Override
    public Response subscribe( String email, SubscriptionRequest request) throws  Exception{
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Fund fund = fundRepository.findById(request.getFundId())
                .orElseThrow(() -> new Exception("Fund not found"));


            if ((user.getBalance().compareTo(fund.getMinimumAmount()) < 0) || (user.getBalance().compareTo(request.getAmount())<0)) {
                throw new Exception(
                        "No tiene saldo disponible para vincularse al fondo " + fund.getName()
                );
            }
            if(request.getAmount().compareTo(fund.getMinimumAmount())<0){
                throw new Exception(
                        "El monto minimo para este fondo es de: " + fund.getMinimumAmount()
                );
            }


        user.setBalance(
                user.getBalance().subtract(request.getAmount())
        );

        Optional<Investment> existingInvestment = user.getInvestments()
                .stream()
                .filter(inv -> inv.getFundId().equals(fund.getId()))
                .findFirst();

        if (existingInvestment.isPresent()) {
            existingInvestment.get().setAmount(
                    existingInvestment.get().getAmount().add(request.getAmount())
            );
        } else {
            Investment newInvestment = new Investment();
            newInvestment.setFundId(fund.getId());
            newInvestment.setAmount(request.getAmount());
            newInvestment.setFundName(fund.getName());
            newInvestment.setActive(true);
            newInvestment.setOpeningDate(LocalDateTime.now());
            user.getInvestments().add(newInvestment);
        }

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(TransactionType.OPENING)
                .fundName(fund.getName())
                .customerId(user.getId())
                .date(LocalDateTime.now())
                .fundId(fund.getId())
                .build();

        transactionRespository.save(transaction);
        userRepository.save(user);
                return new Response("Suscripción exitosa");

    }




    @Override
    public List<?> getAll(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Transaction> transactions= transactionRespository.findByCustomerId(user.getId());

        return transactions;
    }
}
