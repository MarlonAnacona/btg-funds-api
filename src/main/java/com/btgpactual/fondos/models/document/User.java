package com.btgpactual.fondos.models.document;

import com.btgpactual.fondos.models.embedded.Investment;
import com.btgpactual.fondos.models.enums.NotificationPreference;
import com.btgpactual.fondos.models.enums.Role;
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
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String email;

    private String password;

    private Role role;

    private String name;

    private BigDecimal balance;

    private NotificationPreference notificationPreference;

    private List<Investment> investments;
}
