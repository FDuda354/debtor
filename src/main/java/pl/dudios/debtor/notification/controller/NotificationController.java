package pl.dudios.debtor.notification.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.notification.model.Notification;
import pl.dudios.debtor.notification.service.NotificationService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/last/user")
    public ResponseEntity<List<Notification>> getNotificationsByUserId(@AuthenticationPrincipal Customer customer) {
        return ResponseEntity.ok(notificationService.getLastNotificationsByCustomerId(customer.getId()));
    }


    @PostMapping("/read/user")
    public ResponseEntity<Void> readNotificationsByUserId(@AuthenticationPrincipal Customer customer) {
        notificationService.readNotificationsByCustomerId(customer.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteNotificationById(@RequestParam("id") Long id) {
        notificationService.deleteNotificationById(id);
        return ResponseEntity.noContent().build();
    }
}
