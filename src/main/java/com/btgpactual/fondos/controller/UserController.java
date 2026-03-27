package com.btgpactual.fondos.controller;


import com.btgpactual.fondos.models.dto.RegisterRequest;

import com.btgpactual.fondos.services.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/User")
@CrossOrigin("*")
public class UserController {

    private final IUserService iUserService;


    public UserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        return ResponseEntity.ok(iUserService.create(request));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<?> getAll() {

        return ResponseEntity.ok(iUserService.get());

    }
}
