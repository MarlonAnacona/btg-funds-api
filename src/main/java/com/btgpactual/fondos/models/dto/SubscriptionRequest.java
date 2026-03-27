package com.btgpactual.fondos.models.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubscriptionRequest {

    private String customerId;

    private String fundId;

    private BigDecimal amount;
}