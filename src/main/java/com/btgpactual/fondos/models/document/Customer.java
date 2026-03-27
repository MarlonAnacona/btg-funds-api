package com.btgpactual.fondos.models.document;

import com.btgpactual.fondos.models.embedded.Investement;
import com.btgpactual.fondos.models.enums.NotificationPreference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customers")
public class Customer {
    @Id
    private String id;

    private String name;

    private BigDecimal balance;

    private NotificationPreference notificationPreference;

    private List<Investement> investments;
}