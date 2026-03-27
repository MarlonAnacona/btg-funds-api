package com.btgpactual.fondos.services.impl;

import com.btgpactual.fondos.models.document.Fund;
import com.btgpactual.fondos.models.document.Transaction;
import com.btgpactual.fondos.models.document.User;
import com.btgpactual.fondos.models.dto.CancellationRequest;
import com.btgpactual.fondos.models.dto.NotificationRequest;
import com.btgpactual.fondos.models.dto.Response;
import com.btgpactual.fondos.models.dto.SubscriptionRequest;
import com.btgpactual.fondos.models.embedded.Investment;
import com.btgpactual.fondos.models.enums.NotificationPreference;
import com.btgpactual.fondos.models.enums.TransactionType;
import com.btgpactual.fondos.repositories.FundRepository;
import com.btgpactual.fondos.repositories.TransactionRespository;
import com.btgpactual.fondos.repositories.UserRepository;
import com.btgpactual.fondos.services.IInvestment;
import com.btgpactual.fondos.services.notification.NotificationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InvestmentImpl implements IInvestment {


    public static final String USER_NOT_FOUND = "User not found";
    public static final String SUSCRIPCIÓN_EXITOSA = "Suscripción exitosa";
    public static final String SUSCRIPCIÓN = "Suscripción";
    public static final String FUND_NOT_FOUND = "Fund not found";
    public static final String NO_AMOUNT_NECESARY = "No tiene saldo disponible para vincularse al fondo ";
    public static final String NOT_MINIMUM_AMOUNT = "El monto minimo para este fondo es de: ";
    public static final String NO_SING_FUND = "No está suscrito a este fondo";
    @Autowired
    UserRepository userRepository;
    @Autowired
    FundRepository fundRepository;

    @Autowired
    TransactionRespository transactionRespository;

    @Autowired
    NotificationFactory notificationFactory;

    /**
     * Cancels a user's investment in a fund.
     *
     * Updates the user's balance, removes the investment, saves a transaction, and persists the changes.
     *
     * @param email   User's email to identify the account.
     * @param request CancellationRequest containing the fund ID to cancel.
     * @return Response Message indicating the cancellation was successful.
     * @throws RuntimeException if the user, fund, or investment is not found.
     */
    @Override
    public Response cancellation( String email, CancellationRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));


        Fund fund = fundRepository.findById(request.getFundId())
                .orElseThrow(() -> new RuntimeException(FUND_NOT_FOUND));

      Investment investment = user.getInvestments().stream()
                .filter(inv -> inv.getFundId().equals(fund.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(NO_SING_FUND));

        user.setBalance(
                user.getBalance().add(investment.getAmount())
        );

        user.getInvestments().remove(investment);

        Transaction transaction = createTransaction(user, fund, investment, TransactionType.CANCELLATION);
        transactionRespository.save(transaction);

        transactionRespository.save(transaction);

        userRepository.save(user);


        return new Response("Cancelación exitosa");


    }


    /**
     * Subscribes a user to a fund.
     *
     * Validates that the user has sufficient balance and that the subscription amount meets the fund minimum.
     * Updates user's balance, creates or updates an investment, logs a transaction, and sends a notification.
     *
     * @param email   User's email to identify the account.
     * @param request SubscriptionRequest containing the fund ID and amount to invest.
     * @return Response Message indicating subscription success.
     * @throws Exception if the user or fund is not found, or if the user has insufficient balance or amount below minimum.
     */
    @Override
    public Response subscribe( String email, SubscriptionRequest request) throws  Exception{
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));


        Fund fund = fundRepository.findById(request.getFundId())
                .orElseThrow(() -> new Exception(FUND_NOT_FOUND));


            if ((user.getBalance().compareTo(fund.getMinimumAmount()) < 0) || (user.getBalance().compareTo(request.getAmount())<0)) {
                throw new Exception(
                        NO_AMOUNT_NECESARY + fund.getName()
                );
            }
            if(request.getAmount().compareTo(fund.getMinimumAmount())<0){
                throw new Exception(
                        NOT_MINIMUM_AMOUNT + fund.getMinimumAmount()
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

        Transaction transaction = createTransaction(user, fund, request.getAmount(), TransactionType.OPENING);
        transactionRespository.save(transaction);

        userRepository.save(user);
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setMessage(fund.getName());
        if(user.getNotificationPreference().equals(NotificationPreference.EMAIL)){
            notificationRequest.setTo(user.getEmail());
        }else{
            notificationRequest.setTo(user.getCelphone());

        }
        notificationRequest.setTo(user.getEmail());
        notificationRequest.setSubject(SUSCRIPCIÓN);
        notificationRequest.setType(user.getNotificationPreference());
        notificationFactory.getService(notificationRequest);

                return new Response(SUSCRIPCIÓN_EXITOSA);

    }




    /**
     * Retrieves all transactions for a given user.
     *
     * @param email User's email to identify the account.
     * @return List<Transaction> List of transactions for the user.
     * @throws RuntimeException if the user is not found.
     */
    @Override
    public List<Transaction> getAll(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));

        List<Transaction> transactions= transactionRespository.findByCustomerId(user.getId());

        return transactions;
    }

    /**
     * Creates a transaction record based on a user's investment.
     *
     * @param user       User performing the transaction.
     * @param fund       Fund related to the transaction.
     * @param investment Investment object containing the amount.
     * @param type       Type of transaction (OPENING or CANCELLATION).
     * @return Transaction Transaction record ready to be saved.
     */
    private static Transaction createTransaction(User user, Fund fund, Investment investment, TransactionType type) {
        return Transaction.builder()
                .amount(investment.getAmount())
                .type(type)
                .fundName(fund.getName())
                .customerId(user.getId())
                .fundId(fund.getId())
                .date(LocalDateTime.now())
                .build();
    }

    /**
     * Creates a transaction record based on a given amount (for subscriptions).
     *
     * @param user   User performing the transaction.
     * @param fund   Fund related to the transaction.
     * @param amount Amount involved in the transaction.
     * @param type   Type of transaction (OPENING or CANCELLATION).
     * @return Transaction Transaction record ready to be saved.
     */
    private static Transaction createTransaction(User user, Fund fund, java.math.BigDecimal amount, TransactionType type) {
        return Transaction.builder()
                .amount(amount)
                .type(type)
                .fundName(fund.getName())
                .customerId(user.getId())
                .fundId(fund.getId())
                .date(LocalDateTime.now())
                .build();
    }
}
