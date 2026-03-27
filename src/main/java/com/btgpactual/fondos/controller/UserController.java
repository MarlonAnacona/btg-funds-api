package com.btgpactual.fondos.controller;


import com.btgpactual.fondos.models.dto.RegisterRequest;

import com.btgpactual.fondos.models.dto.Response;
import com.btgpactual.fondos.services.IUserService;
import org.springframework.http.HttpStatus;
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

       try{
           return ResponseEntity.ok(iUserService.create(request));

        }catch (Exception e){
            Response errorDTO= new Response();
            errorDTO.setResponse(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorDTO);
    }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        try{
        return ResponseEntity.ok(iUserService.get());
    }catch (Exception e){
        Response errorDTO= new Response();
        errorDTO.setResponse(e.getMessage());
        return  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorDTO);
    }
    }
}
