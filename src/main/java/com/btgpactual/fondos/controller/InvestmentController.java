package com.btgpactual.fondos.controller;

import com.btgpactual.fondos.models.dto.CancellationRequest;
import com.btgpactual.fondos.models.dto.Response;
import com.btgpactual.fondos.models.dto.SubscriptionRequest;
import com.btgpactual.fondos.services.IInvestment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/investment")
@RequiredArgsConstructor
public class InvestmentController {

    private final IInvestment investmentService;

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping
    public ResponseEntity<?> getFunds(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(investmentService.getAll(email));
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(
            @RequestBody SubscriptionRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        try{
            return ResponseEntity.ok(investmentService.subscribe( email,request));

        }catch (Exception e){
            Response errorDTO= new Response();
            errorDTO.setResponse(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorDTO);

        }
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/cancel")
    public ResponseEntity<?> cancellation(
            @RequestBody CancellationRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        try{
        return ResponseEntity.ok(investmentService.cancellation( email,request));
    }catch (Exception e){
        Response errorDTO= new Response();
        errorDTO.setResponse(e.getMessage());
        return  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorDTO);
    }
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/transactions")
    public ResponseEntity<?> getAllTransactions(Authentication authentication) {
        String email = authentication.getName();
        try{
            return ResponseEntity.ok(investmentService.getAll(email));
    }catch (Exception e){
        Response errorDTO= new Response();
        errorDTO.setResponse(e.getMessage());
        return  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorDTO);

    }
    }
}