package it.unisannio.trip.controller;

import it.unisannio.trip.service.NotificationDispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationsController {

    private final NotificationDispatcherService dispatcher;

    @Autowired
    public NotificationsController(NotificationDispatcherService dispatcher) {
        this.dispatcher = dispatcher;
    }
    @MessageMapping("/start")
    public void start(StompHeaderAccessor stompHeaderAccessor) {
        dispatcher.add(stompHeaderAccessor.getSessionId());
    }
    @MessageMapping("/stop")
    public void stop(StompHeaderAccessor stompHeaderAccessor) {
        dispatcher.remove(stompHeaderAccessor.getSessionId());
    }
}
