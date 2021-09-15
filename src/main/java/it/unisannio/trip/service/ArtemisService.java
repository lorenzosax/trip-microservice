package it.unisannio.trip.service;

import it.unisannio.trip.dto.internal.TripDTO;
import it.unisannio.trip.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableJms
public class ArtemisService {

    @Value("${jms.topic.trip-request}")
    private String tripRequestTopic;

    private JmsTemplate jmsTemplate;
    private WebSocketService webSocketService;


    @Autowired
    public ArtemisService(JmsTemplate jmsTemplate, WebSocketService webSocketService) {
        this.jmsTemplate = jmsTemplate;
        this.webSocketService = webSocketService;
    }

    @Transactional
    public void sendTrip(String sessionId, Trip trip){
        TripDTO tripDTO = new TripDTO();
        tripDTO.setId(trip.getId());
        tripDTO.setSource(trip.getSource().getNodeId());
        tripDTO.setDestination(trip.getDestination().getNodeId());
        tripDTO.setRequestDate(trip.getRequestDate());

        this.webSocketService.addRequestTripId(sessionId, trip.getId());
        jmsTemplate.convertAndSend(tripRequestTopic, tripDTO);
    }
}
