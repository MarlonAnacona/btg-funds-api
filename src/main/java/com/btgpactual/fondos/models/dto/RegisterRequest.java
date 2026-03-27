package com.btgpactual.fondos.models.dto;

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
public class RegisterRequest {


        private String email;

        private String password;

        private String name;

        private NotificationPreference notificationPreference;

        private String celphone;

        private Role role;

}
