package com.btgpactual.fondos.services.notification;

import com.btgpactual.fondos.models.dto.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificacionServiceImpl implements NotificacionService{

    private final JavaMailSender mailSender;

    private final String fromAddress;


    @Autowired
    public EmailNotificacionServiceImpl(JavaMailSender mailSender,
                                        @Value("${notification.email.from}") String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    /**
     * Sends an email notification based on the provided NotificationRequest.
     *
     * The email will be sent from a fixed "no-reply" address.
     *
     * @param request NotificationRequest containing the recipient email, subject, and message content.
     * @throws RuntimeException if sending the email fails.
     */
    @Override
    public void send(NotificationRequest request) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();

            mail.setFrom(fromAddress);
            mail.setTo(request.getTo());
            mail.setSubject(request.getSubject());
            mail.setText("Se ha inscrito al fondo: "+request.getMessage());

            mailSender.send(mail);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando Email: " + e.getMessage());
        }
    }
    }

