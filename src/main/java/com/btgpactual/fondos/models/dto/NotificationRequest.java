package com.btgpactual.fondos.models.dto;

import com.btgpactual.fondos.models.enums.NotificationPreference;
import lombok.Data;

@Data
public class NotificationRequest {
    private NotificationPreference type;
    private String to;
    private String subject;
    private String message;
}