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
import java.util.stream.Collectors;


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

    public Trip sendRequestToMOM(String sessionId, TripRequestDTO requestDTO) {
        Trip savedTrip = null;
        if (this.isFeasibleRequest(requestDTO)) {
            Trip trip = new Trip();
            trip.setSource(requestDTO.getOsmidSource());
            trip.setDestination(requestDTO.getOsmidDestination());
            savedTrip = this.tripRepository.save(trip);
            this.artemisService.sendTrip(sessionId, savedTrip);
        }
        return savedTrip;
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

            routeStatsDTO.setStations(stationStatsList.stream()
                    .sorted(Comparator.comparing(StationStatsDTO::getRequests).reversed())
                    .collect(Collectors.toList()));
            routeStatsDTO.setRequests(totalRouteRequests);
            allRequests += totalRouteRequests;

            routeStatsList.add(routeStatsDTO);
        }

        routeStatsList = routeStatsList.stream()
                .sorted(Comparator.comparing(RouteStatsDTO::getRequests).reversed())
                .collect(Collectors.toList());

        return new StatisticsDTO(allRequests, routeStatsList);
    }

    private boolean isFeasibleRequest(TripRequestDTO requestDTO) {
        List<Route> routesSrc = this.routeService.getRoutesWithStationIds(List.of(requestDTO.getOsmidSource(), requestDTO.getOsmidDestination()));
        return routesSrc != null && routesSrc.size() > 0;
    }

    private Date getDate24HoursAgo() {
        this.calendar.setTime(new Date());
        this.calendar.add(Calendar.DAY_OF_YEAR,-1);
        return this.calendar.getTime();
    }

}
