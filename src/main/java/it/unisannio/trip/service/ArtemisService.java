package it.unisannio.trip.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisannio.trip.dto.TripNotificationDTO;
import it.unisannio.trip.dto.internal.TripDTO;
import it.unisannio.trip.model.Trip;
import it.unisannio.trip.websocket.WebSocketClientNotFoundException;
import it.unisannio.trip.websocket.WebSocketEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Service
@EnableJms
public class ArtemisService {

    private final Logger logger = LoggerFactory.getLogger(WebSocketEndpoint.class);

    @Value("${jms.topic.trip-request}")
    private String tripRequestTopic;

    private JmsTemplate jmsTemplate;
    private WebSocketService webSocketService;
    private TripService tripService;
    private ObjectMapper objectMapper;


    @Autowired
    public ArtemisService(JmsTemplate jmsTemplate, WebSocketService webSocketService, TripService tripService, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.webSocketService = webSocketService;
        this.tripService = tripService;
        this.objectMapper = objectMapper;
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

    @JmsListener(destination = "${jms.topic.trip-confirmation}")
    private void receive(Message message) throws JMSException, JsonProcessingException {
        if (!(message instanceof TextMessage)) throw new IllegalArgumentException("Message must be of type TextMessage");

        try {
            String json = ((TextMessage) message).getText();
            TripNotificationDTO tripNotification = objectMapper.readValue(json, TripNotificationDTO.class);
            this.tripService.updateTripWithVehicleLicensePlate(tripNotification.getTripId(), tripNotification.getVehicleLicensePlate());
            this.webSocketService.sendMessage(tripNotification.getTripId(), tripNotification);

            logger.debug("TripNotification sent correctly to client");
        } catch (JMSException | JsonMappingException ex) {
            throw new RuntimeException(ex);
        } catch (WebSocketClientNotFoundException e) {
            logger.info(e.getMessage());
        }
    }
}
