package it.unisannio.trip.service;

import it.unisannio.trip.dto.TripNotificationDTO;
import it.unisannio.trip.model.Trip;
import it.unisannio.trip.repository.TripRepository;
import it.unisannio.trip.websocket.WebSocketEndpoint;
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

    @Value("${jms.topic.trip-request}")
    private String tripRequestTopic;

    private JmsTemplate jmsTemplate;
    private TripRepository tripRepository;


    @Autowired
    public ArtemisService(JmsTemplate jmsTemplate, TripRepository tripRepository) {
        this.jmsTemplate = jmsTemplate;
        this.tripRepository = tripRepository;
    }

    public void sendTrip(Trip trip){
        jmsTemplate.convertAndSend(tripRequestTopic, trip, message -> {
            message.setStringProperty("requestId", trip.getId());
            message.setJMSCorrelationID("trip-1");
            return message;
        });
    }

    @JmsListener(destination = "${jms.topic.trip-proposal}", selector = "JMSCorrelationID = 'trip-1'")
    private void receive(Message message) throws JMSException {
        TripNotificationDTO tripNotification = message.getBody(TripNotificationDTO.class);
        // TripNotificationDTO tripNotification = new TripNotificationDTO("60d9940fe043d250acb6dffb", 1, 900);

        Optional<Trip> t = this.tripRepository.findById(tripNotification.getTripId());
        if (t.isPresent()) {
            Trip trip = t.get();
            trip.setStatus(Trip.Status.IN_PROGRESS);
            this.tripRepository.save(trip);

            /*if (!WebSocketEndpoint.peers.isEmpty() && WebSocketEndpoint.peers.get(tripNotification.getTripId()) != null) {
                wsEndpoint.send(tripNotification);
            }*/
        }
    }
}
