package com.btgpactual.fondos.models.dto;

import com.btgpactual.fondos.models.enums.FundCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ResponseFunds {


    private String name;

    private BigDecimal minimumAmount;

    private FundCategory category;
}
