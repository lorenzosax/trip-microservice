package it.unisannio.trip.service;

import it.unisannio.trip.dto.TripNotifyDTO;
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

@Service
@EnableJms
public class ArtemisService {

    @Value("${jms.topic.trip-request}")
    private String tripRequestTopic;

    @Autowired
    private WebSocketEndpoint wsEndpoint;

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

    @JmsListener(destination = "${jms.topic.trip-request}", selector = "JMSCorrelationID = 'trip-1'")
    private void receive(Message message) throws JMSException {
        // riceviamo il trip con informazioni aggiunte (in quanto è stato preso in considerazione da vehicle-service)
        Trip t = message.getBody(Trip.class);
        tripRepository.save(t);

        if (!WebSocketEndpoint.peers.isEmpty() && WebSocketEndpoint.peers.get("0") != null) {
            wsEndpoint.send(new TripNotifyDTO(1, 1, 1, 1), WebSocketEndpoint.peers.get("0").getId());
        }
    }
}
