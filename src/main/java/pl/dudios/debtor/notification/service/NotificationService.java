package pl.dudios.debtor.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.dudios.debtor.customer.service.CustomerService;
import pl.dudios.debtor.notification.model.Notification;
import pl.dudios.debtor.notification.model.Status;
import pl.dudios.debtor.notification.repo.NotificationRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    private final CustomerService customerService;

    public void notify(Notification notification) {
        notification.setStatus(Status.UNREAD);
        notification.setDate(LocalDateTime.now());
        messagingTemplate.convertAndSend("/all/messages", notification.getMessage());
        //notificationRepository.save(notification);
    }

    public void notifyUser(String email, Notification notification) {
        notification.setStatus(Status.UNREAD);
        notification.setDate(LocalDateTime.now());
        notification.setCustomer(customerService.getCustomerByEmail(email));
        messagingTemplate.convertAndSendToUser(notification.getCustomer().getEmail(), "/one/messages", notification.getMessage());
        //notificationRepository.save(notification);

    }


}
