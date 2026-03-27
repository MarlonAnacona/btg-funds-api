package com.btgpactual.fondos.models.document;


import com.btgpactual.fondos.models.enums.FundCategory;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "funds")
public class Fund {

    @Id
    private String id;

    private String name;

    private BigDecimal minimumAmount;

    private FundCategory category;
}