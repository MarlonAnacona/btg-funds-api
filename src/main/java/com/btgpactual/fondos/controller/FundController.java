package com.btgpactual.fondos.controller;

import com.btgpactual.fondos.models.dto.RegisterFunds;
import com.btgpactual.fondos.models.dto.Response;
import com.btgpactual.fondos.services.IFundService;
import org.springframework.http.HttpStatus;
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
    try{
        return ResponseEntity.ok(iFundService.create(request));
    }catch (Exception e){
        Response errorDTO= new Response();
        errorDTO.setResponse(e.getMessage());
        return  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorDTO);
    }
    }

    @GetMapping("/")
    public ResponseEntity<?> getFunds() {
    try{
        return ResponseEntity.ok(iFundService.getAll());
    }catch (Exception e){
        Response errorDTO= new Response();
        errorDTO.setResponse(e.getMessage());
        return  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorDTO);
    }
    }
}
