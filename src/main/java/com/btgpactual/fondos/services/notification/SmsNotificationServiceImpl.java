package com.btgpactual.fondos.services.notification;

import com.btgpactual.fondos.config.TwilioConfig;
import com.btgpactual.fondos.models.dto.NotificationRequest;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsNotificationServiceImpl implements  NotificacionService{

    private final TwilioConfig twilioConfig;

    @Autowired
    public SmsNotificationServiceImpl(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    /**
     * Sends an SMS notification using Twilio API.
     *
     * @param request NotificationRequest containing recipient phone number and message content.
     * @throws RuntimeException if sending the SMS fails.
     */
    @Override
    public void send(NotificationRequest request) {
        try {
            Message.creator(
                    new PhoneNumber(request.getTo()),
                    new PhoneNumber(twilioConfig.getFromNumber()),
                    request.getMessage()
            ).create();

        } catch (Exception e) {
            throw new RuntimeException("Error enviando SMS: " + e.getMessage());
        }
    }
}
