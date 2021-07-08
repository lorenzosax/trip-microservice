package it.unisannio.trip.service;

import it.unisannio.trip.dto.TripNotificationDTO;
import it.unisannio.trip.model.Trip;
import it.unisannio.trip.repository.TripRepository;
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

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@EnableJms
public class ArtemisService {

    private final Logger logger = LoggerFactory.getLogger(WebSocketEndpoint.class);

    private static final String SESSION_ID_PROPERTY = "sessionId";

    @Value("${jms.topic.trip-request}")
    private String tripRequestTopic;

    private JmsTemplate jmsTemplate;
    private TripRepository tripRepository;
    private WebSocketService webSocketService;


    @Autowired
    public ArtemisService(JmsTemplate jmsTemplate, TripRepository tripRepository, WebSocketService webSocketService) {
        this.jmsTemplate = jmsTemplate;
        this.tripRepository = tripRepository;
        this.webSocketService = webSocketService;
    }

    public void sendTrip(String sessionId, Trip trip){
        this.webSocketService.addRequestTripId(sessionId, trip.getId());
        jmsTemplate.convertAndSend(tripRequestTopic, trip);
    }

    @JmsListener(destination = "${jms.topic.trip-confirmation}")
    private void receive(Message message) throws JMSException {
        TripNotificationDTO tripNotification = message.getBody(TripNotificationDTO.class);

        Optional<Trip> trip = this.tripRepository.findById(tripNotification.getTripId());
        if (trip.isPresent()) {
            try {
                this.webSocketService.sendMessage(trip.get().getId(), tripNotification);
                logger.debug("TripNotification sent correctly to client");
            } catch (WebSocketClientNotFoundException e) {
                logger.info(e.getMessage());
            }
        }
    }

    // TODO Only for test - to remove
    @JmsListener(destination = "${jms.topic.trip-request}")
    private void receiveTest(Message message) throws JMSException, InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        Trip trip = message.getBody(Trip.class);

        TripNotificationDTO tripNotificationDTO = new TripNotificationDTO(trip.getId(), "TARGA", trip.getSource());
        try {
            this.webSocketService.sendMessage(trip.getId(), tripNotificationDTO);
            logger.info("TripNotification sent correctly to client");
        } catch (WebSocketClientNotFoundException e) {
            logger.info(e.getMessage());
        }
    }
}
