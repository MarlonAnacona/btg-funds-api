package com.btgpactual.fondos.models.dto;

import com.btgpactual.fondos.models.enums.FundCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class RegisterFunds {
    private String name;

    private BigDecimal minimumAmount;

    private FundCategory category;
}
