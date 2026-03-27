package com.btgpactual.fondos.models.notification;


import com.btgpactual.fondos.models.enums.NotificationPreference;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    private String customerId;

    private String message;

    private NotificationPreference type;

    private LocalDateTime date;
}