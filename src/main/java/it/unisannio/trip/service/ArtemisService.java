package it.unisannio.trip.service;

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
        jmsTemplate.convertAndSend(tripRequestTopic, trip);
    }

    @JmsListener(destination = "${jms.topic.trip-proposal}")
    private void receive(Message message) throws JMSException {
        // riceviamo il trip con informazioni aggiunte (in quanto è stato preso in considerazione da vehicle-service)
        Trip t = message.getBody(Trip.class);
        tripRepository.save(t);
    }
}
