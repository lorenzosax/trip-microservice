package it.unisannio.trip.service;

import it.unisannio.trip.dto.TripNotificationDTO;
import it.unisannio.trip.model.Trip;
import it.unisannio.trip.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.Optional;

@Service
@EnableJms
public class ArtemisService {

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
        jmsTemplate.convertAndSend(tripRequestTopic, trip, message -> {
            message.setStringProperty(SESSION_ID_PROPERTY, sessionId);
            message.setJMSCorrelationID("trip-1");
            return message;
        });
    }

    @JmsListener(destination = "${jms.topic.trip-proposal}", selector = "JMSCorrelationID = 'trip-1'")
    private void receive(Message message) throws JMSException {
        TripNotificationDTO tripNotification = message.getBody(TripNotificationDTO.class);

        Optional<Trip> t = this.tripRepository.findById(tripNotification.getTripId());
        if (t.isPresent()) {
            Trip trip = t.get();
            trip.setStatus(Trip.Status.IN_PROGRESS);
            this.tripRepository.save(trip);

            String sessionId = message.getStringProperty(SESSION_ID_PROPERTY);
            this.webSocketService.sendMessage(sessionId, tripNotification);
        }
    }

    // TODO Only for test - to remove
    @JmsListener(destination = "${jms.topic.trip-request}", selector = "JMSCorrelationID = 'trip-1'")
    private void receiveTest(Message message) throws JMSException {
        Trip trip = message.getBody(Trip.class);

        TripNotificationDTO tripNotificationDTO = new TripNotificationDTO("FG290PO", 2, trip.getSource());
        String sessionId = message.getStringProperty(SESSION_ID_PROPERTY);
        this.webSocketService.sendMessage(sessionId, tripNotificationDTO);
    }
}
