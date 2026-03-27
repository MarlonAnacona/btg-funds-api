package com.btgpactual.fondos.models.document;


import com.btgpactual.fondos.models.enums.TransactionType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;

    private String customerId;

    private String fundId;

    private String fundName;

    private TransactionType type;

    private BigDecimal amount;

    private LocalDateTime date;
}