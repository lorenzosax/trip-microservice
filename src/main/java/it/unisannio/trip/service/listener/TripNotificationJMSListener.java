package it.unisannio.trip.service.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisannio.trip.dto.TripNotificationDTO;
import it.unisannio.trip.service.TripService;
import it.unisannio.trip.service.WebSocketService;
import it.unisannio.trip.websocket.WebSocketClientNotFoundException;
import it.unisannio.trip.websocket.WebSocketEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Service
public class TripNotificationJMSListener {

    private final Logger logger = LoggerFactory.getLogger(WebSocketEndpoint.class);

    private ObjectMapper objectMapper;
    private WebSocketService webSocketService;
    private TripService tripService;

    @Autowired
    public TripNotificationJMSListener(ObjectMapper objectMapper, WebSocketService webSocketService, TripService tripService) {
        this.objectMapper = objectMapper;
        this.webSocketService = webSocketService;
        this.tripService = tripService;
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
