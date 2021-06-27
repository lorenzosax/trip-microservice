package it.unisannio.trip.service;

import it.unisannio.trip.dto.TripRequestDTO;
import it.unisannio.trip.model.Trip;
import it.unisannio.trip.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TripService {

    private TrafficService trafficService;
    private TripRepository tripRepository;
    private ArtemisService artemisService;

    @Autowired
    public TripService(TrafficService trafficService, TripRepository tripRepository, ArtemisService artemisService) {
        this.trafficService = trafficService;
        this.tripRepository = tripRepository;
        this.artemisService = artemisService;
    }

    public boolean appendNewRequest(TripRequestDTO requestDTO, Integer userId) {
        // List<Coordinate> shortestPath = trafficService.shortestPath(requestDTO.getOsmidSource(), requestDTO.getOsmidDestination());

        Trip trip = new Trip();
        trip.setSource(requestDTO.getOsmidSource());
        trip.setDestination(requestDTO.getOsmidDestination());
        trip.setUserId(userId);
        this.tripRepository.save(trip);
        this.artemisService.sendTrip(trip);

        // mandare in topic questa richiesta
        return true;
    }


    public void confirmTrip(Integer tripId, Integer userId) {
        // salvare in db la proposta accettata del trip
    }
}
