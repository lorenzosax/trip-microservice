package it.unisannio.trip.service;

import it.unisannio.trip.dto.TripRequestDTO;
import it.unisannio.trip.model.Trip;
import it.unisannio.trip.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TripService {

    private TripRepository tripRepository;
    private ArtemisService artemisService;

    @Autowired
    public TripService(TripRepository tripRepository, ArtemisService artemisService) {
        this.tripRepository = tripRepository;
        this.artemisService = artemisService;
    }

    public String appendNewRequest(TripRequestDTO requestDTO) {
        Trip trip = new Trip();
        trip.setSource(requestDTO.getOsmidSource());
        trip.setDestination(requestDTO.getOsmidDestination());
        Trip savedTrip = this.tripRepository.save(trip);
        this.artemisService.sendTrip(savedTrip);

        return savedTrip.getId();
    }

}
