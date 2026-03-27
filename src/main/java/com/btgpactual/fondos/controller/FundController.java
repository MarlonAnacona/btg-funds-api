package com.btgpactual.fondos.controller;

import com.btgpactual.fondos.models.dto.RegisterFunds;
import com.btgpactual.fondos.services.IFundService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/Funds")
@CrossOrigin("*")
public class FundController {

    private final IFundService iFundService;

    public FundController(IFundService iFundService) {
        this.iFundService = iFundService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterFunds request) {

        return ResponseEntity.ok(iFundService.create(request));

    }

    @GetMapping("/")
    public ResponseEntity<?> getFunds() {

        return ResponseEntity.ok(iFundService.getAll());

    }
}
