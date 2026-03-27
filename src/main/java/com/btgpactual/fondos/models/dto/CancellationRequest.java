package com.btgpactual.fondos.models.dto;


import lombok.Data;

@Data
public class CancellationRequest {

    private String customerId;

    private String fundId;
}