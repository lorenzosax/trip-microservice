package it.unisannio.trip.service;

import it.unisannio.trip.dto.TripRequestDTO;
import it.unisannio.trip.model.Route;
import it.unisannio.trip.model.Trip;
import it.unisannio.trip.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TripService {

    private TripRepository tripRepository;
    private ArtemisService artemisService;
    private RouteService routeService;

    @Autowired
    public TripService(TripRepository tripRepository, ArtemisService artemisService, RouteService routeService) {
        this.tripRepository = tripRepository;
        this.artemisService = artemisService;
        this.routeService = routeService;
    }

    public boolean appendNewRequest(TripRequestDTO requestDTO) {
        if (this.isFeasibleRequest(requestDTO)) {
            Trip trip = new Trip();
            trip.setSource(requestDTO.getOsmidSource());
            trip.setDestination(requestDTO.getOsmidDestination());
            Trip savedTrip = this.tripRepository.save(trip);
            this.artemisService.sendTrip(savedTrip);

            return true;
        }
        return false;
    }

    private boolean isFeasibleRequest(TripRequestDTO requestDTO) {

        List<Route> routesSrc = this.routeService.getRoutesByStationId(requestDTO.getOsmidSource());
        if (routesSrc != null && routesSrc.size() > 0) {
            List<Route> routesDst = this.routeService.getRoutesByStationId(requestDTO.getOsmidDestination());
            if (routesDst != null && routesDst.size() > 0) {
                for (Route route : routesSrc) {
                    if (routesDst.contains(route)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
