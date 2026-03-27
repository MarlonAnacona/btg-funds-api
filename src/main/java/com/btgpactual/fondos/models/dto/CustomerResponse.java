package com.btgpactual.fondos.models.dto;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CustomerResponse {

    private String id;

    private String name;

    private String email;

    private BigDecimal balance;
}