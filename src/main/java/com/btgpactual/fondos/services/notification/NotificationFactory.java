package com.btgpactual.fondos.services.notification;

import com.btgpactual.fondos.models.document.User;
import com.btgpactual.fondos.models.dto.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationFactory {

    private final EmailNotificacionServiceImpl emailService;
    private final SmsNotificationServiceImpl smsService;

    @Autowired
    public NotificationFactory(EmailNotificacionServiceImpl emailService, SmsNotificationServiceImpl smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }

    public NotificacionService getService(NotificationRequest preference) {
        if (preference.getType().toString().equals("EMAIL")) {
              emailService.send(preference);
        } else if ("SMS".equalsIgnoreCase(preference.getType().toString())) {
             smsService.send(preference);
        }

        return null;
    }
}