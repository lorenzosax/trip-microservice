package it.unisannio.trip.service;

import it.unisannio.trip.dto.TripRequestDTO;
import it.unisannio.trip.dto.internal.RouteStatsDTO;
import it.unisannio.trip.dto.internal.StationStatsDTO;
import it.unisannio.trip.dto.internal.StatisticsDTO;
import it.unisannio.trip.model.Route;
import it.unisannio.trip.model.Station;
import it.unisannio.trip.model.Trip;
import it.unisannio.trip.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class TripService {

    private TripRepository tripRepository;
    private ArtemisService artemisService;
    private RouteService routeService;
    private Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    @Autowired
    public TripService(TripRepository tripRepository, ArtemisService artemisService, RouteService routeService) {
        this.tripRepository = tripRepository;
        this.artemisService = artemisService;
        this.routeService = routeService;
    }

    public boolean sendRequest(String sessionId, TripRequestDTO requestDTO) {
        if (this.isFeasibleRequest(requestDTO)) {
            Trip trip = new Trip();
            trip.setSource(requestDTO.getOsmidSource());
            trip.setDestination(requestDTO.getOsmidDestination());
            Trip savedTrip = this.tripRepository.save(trip);
            this.artemisService.sendTrip(sessionId, savedTrip);

            return true;
        }
        return false;
    }

    public StatisticsDTO getStatistics() {
        Date yesterday = getDate24HoursAgo();
        List<RouteStatsDTO> routeStatsList = new ArrayList<>();
        List<Route> routes = this.routeService.getRawRoutes();
        int allRequests = 0;

        for (Route route : routes) {
            RouteStatsDTO routeStatsDTO = new RouteStatsDTO();
            routeStatsDTO.setId(route.getId());

            int totalRouteRequests = 0;
            List<StationStatsDTO> stationStatsList = new ArrayList<>();

            for (Station station : route.getStations()) {
                StationStatsDTO stationStatsDTO = new StationStatsDTO();
                stationStatsDTO.setNodeId(station.getNodeId());
                stationStatsDTO.setPosition(station.getPosition());

                int stationRequests = this.tripRepository.countBySourceAndRequestDateIsGreaterThan(station.getNodeId(), yesterday);
                stationStatsDTO.setRequests(stationRequests);
                totalRouteRequests += stationRequests;

                stationStatsList.add(stationStatsDTO);
            }
            routeStatsDTO.setStations(stationStatsList);
            routeStatsDTO.setRequests(totalRouteRequests);
            allRequests += totalRouteRequests;

            routeStatsList.add(routeStatsDTO);
        }

        return new StatisticsDTO(allRequests, routeStatsList);
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

    private Date getDate24HoursAgo() {
        this.calendar.setTime(new Date());
        this.calendar.add(Calendar.DAY_OF_YEAR,-1);
        return this.calendar.getTime();
    }

}
