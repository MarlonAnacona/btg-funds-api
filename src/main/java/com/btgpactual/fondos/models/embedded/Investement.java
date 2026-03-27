package com.btgpactual.fondos.models.embedded;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Investement {

    private String fundId;

    private String fundName;

    private BigDecimal amount;

    private LocalDateTime openingDate;

    private boolean active;
}