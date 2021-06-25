package it.unisannio.trip.service;

import it.unisannio.trip.dto.TripNotifyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class NotificationDispatcherService {

    private final SimpMessagingTemplate template;
    private Set<String> listeners = new HashSet<>();

    @Autowired
    public NotificationDispatcherService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void add(String sessionId) {
        listeners.add(sessionId);
    }

    public void remove(String sessionId) {
        listeners.remove(sessionId);
    }

    @Scheduled(fixedDelay = 2000)
    public void dispatch() {
        for (String listener : listeners) {

            SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
            headerAccessor.setSessionId(listener);
            headerAccessor.setLeaveMutable(true);

            template.convertAndSendToUser(
                    listener,
                    "/topic/proposal",
                    new TripNotifyDTO(1, 1, 1, 1),
                    headerAccessor.getMessageHeaders());
        }
    }
}
