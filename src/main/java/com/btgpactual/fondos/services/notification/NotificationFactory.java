package com.btgpactual.fondos.services.notification;

import org.springframework.stereotype.Service;

@Service
public class NotificationFactory {

    private final EmailNotificacionServiceImpl emailService;
    private final SmsNotificationServiceImpl smsService;

    public NotificationFactory(EmailNotificacionServiceImpl emailService, SmsNotificationServiceImpl smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }

    public NotificacionService getService(String preference) {
        if ("EMAIL".equalsIgnoreCase(preference)) {
            return emailService;
        } else if ("SMS".equalsIgnoreCase(preference)) {
            return smsService;
        }
        throw new RuntimeException("Preferencia inválida");
    }
}