package it.unisannio.trip.service;

import it.unisannio.trip.model.Trip;
import it.unisannio.trip.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
@EnableJms
public class ArtemisService {

    @Value("${jms.queue.trip-request}")
    private String tripRequestQueue;

    @Value("${jms.queue.trip-proposal}")
    private String proposalQueue;

    private JmsTemplate jmsTemplate;
    private TripRepository tripRepository;

    @Autowired
    public ArtemisService(JmsTemplate jmsTemplate, TripRepository tripRepository) {
        this.jmsTemplate = jmsTemplate;
        this.tripRepository = tripRepository;
    }

    public void sendTrip(Trip trip){
        jmsTemplate.convertAndSend(tripRequestQueue, trip);
    }

    @JmsListener(destination = "${jms.queue.trip-request}")
    private void receive(Message message) throws JMSException {
        // riceviamo il trip con informazioni aggiunte (in quanto è stato preso in considerazione da vehicle-service)
        Trip t = message.getBody(Trip.class);
        tripRepository.save(t);
    }
}
