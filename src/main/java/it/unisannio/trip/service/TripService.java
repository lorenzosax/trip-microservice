package it.unisannio.trip.service;

import it.unisannio.trip.dto.ConfirmationDTO;
import it.unisannio.trip.dto.TripRequestDTO;
import it.unisannio.trip.dto.internal.RouteStatsDTO;
import it.unisannio.trip.dto.internal.StationStatsDTO;
import it.unisannio.trip.dto.internal.StatisticsDTO;
import it.unisannio.trip.dto.internal.TripDTO;
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

    private Trip sendRequestToMOM(String sessionId, TripRequestDTO requestDTO) {

        Trip trip = new Trip();
        trip.setSource(requestDTO.getOsmidSource());
        trip.setDestination(requestDTO.getOsmidDestination());
        trip = this.tripRepository.save(trip);
        this.artemisService.sendTrip(sessionId, trip);

        return trip;
    }

    public ConfirmationDTO checkTripRequest(String sessionId, TripRequestDTO tripRequestDTO) {

        ConfirmationDTO confirmation = null;

        List<Route> routes = this.routeService.getRoutesWithStationIds(List.of(tripRequestDTO.getOsmidSource(), tripRequestDTO.getOsmidDestination()));
        if(routes != null && routes.size() == 1) {
            this.sendRequestToMOM(sessionId, tripRequestDTO);
            confirmation = new ConfirmationDTO(ConfirmationDTO.Status.APPROVED);
        } else {
            Route routeSrc = this.routeService.getRouteByStationId(tripRequestDTO.getOsmidSource());
            Route routeDst = this.routeService.getRouteByStationId(tripRequestDTO.getOsmidDestination());

            if(routeSrc != null && routeDst != null) {
                List<Station> stations = routeSrc.getReachableRoutes().get(routeDst.getId());
                if (stations != null && stations.size() > 0) {
                    List<TripDTO> trips = new ArrayList<TripDTO>();

                    TripDTO tripDTO = new TripDTO();
                    tripDTO.setSource(tripRequestDTO.getOsmidSource());
                    for(Station s : stations) {
                        tripDTO.setDestination(s.getNodeId());
                        trips.add(tripDTO);
                        tripDTO = new TripDTO();
                        tripDTO.setSource(s.getNodeId());
                    }
                    tripDTO.setDestination(tripRequestDTO.getOsmidDestination());
                    trips.add(tripDTO);

                    confirmation = new ConfirmationDTO(ConfirmationDTO.Status.MULTI_PATHS, trips);

                } else {
                    confirmation = new ConfirmationDTO(ConfirmationDTO.Status.REJECTED);
                }
            }
        }
        return confirmation;
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

    private Date getDate24HoursAgo() {
        this.calendar.setTime(new Date());
        this.calendar.add(Calendar.DAY_OF_YEAR,-1);
        return this.calendar.getTime();
    }

}
