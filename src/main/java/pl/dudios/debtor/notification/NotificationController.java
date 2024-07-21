package pl.dudios.debtor.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

//    @MessageMapping("/application")
//    @SendTo("/all/messages")
//    public String sendNotification(String message) {
//        System.out.println("Sending notification: " + message);
//        return "Powiadomienie - " + message;
//    }
//
    @MessageMapping("/private")
    public void sendNotificationToUser(@Payload String message) {
        messagingTemplate.convertAndSendToUser("filipduda9@icloud.com", "/one/messages", message);
        System.out.println("Sending private notification: " + message);
    }

}
