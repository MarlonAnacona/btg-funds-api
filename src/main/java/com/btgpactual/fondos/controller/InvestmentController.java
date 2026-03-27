package com.btgpactual.fondos.controller;

import com.btgpactual.fondos.models.dto.CancellationRequest;
import com.btgpactual.fondos.models.dto.SubscriptionRequest;
import com.btgpactual.fondos.services.IInvestment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/funds")
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
        return ResponseEntity.ok(investmentService.subscribe(email, request));
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/cancel")
    public ResponseEntity<?> cancel(
            @RequestBody CancellationRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(investmentService.cancellation(email, request));
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/transactions")
    public ResponseEntity<?> transactions(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(investmentService.getAll(email));
    }
}