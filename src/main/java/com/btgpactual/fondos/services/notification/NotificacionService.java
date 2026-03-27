package com.btgpactual.fondos.services.notification;

import com.btgpactual.fondos.models.dto.NotificationRequest;

public interface NotificacionService {

    void send(NotificationRequest request);
}
