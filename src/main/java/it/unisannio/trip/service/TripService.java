package it.unisannio.trip.service;

import it.unisannio.trip.dto.ConfirmationDTO;
import it.unisannio.trip.dto.StationDTO;
import it.unisannio.trip.dto.TripRequestDTO;
import it.unisannio.trip.dto.external.MessinaBookingRequestDTO;
import it.unisannio.trip.dto.internal.*;
import it.unisannio.trip.model.Route;
import it.unisannio.trip.model.Station;
import it.unisannio.trip.model.Trip;
import it.unisannio.trip.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class TripService {

    private TripRepository tripRepository;
    private ArtemisService artemisService;
    private MessinaService messinaService;
    private RouteService routeService;
    private Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    @Autowired
    public TripService(TripRepository tripRepository, ArtemisService artemisService, MessinaService messinaService, RouteService routeService) {
        this.tripRepository = tripRepository;
        this.artemisService = artemisService;
        this.messinaService = messinaService;
        this.routeService = routeService;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Trip sendRequestToExternal(String sessionId, TripRequestDTO requestDTO) {

        Trip trip = new Trip();

        StationDTO srcStationDTO = this.routeService.getStationInfo(requestDTO.getOsmidSource());
        StationDTO dstStationDTO = this.routeService.getStationInfo(requestDTO.getOsmidDestination());

        trip.setSource(new Station(srcStationDTO.getNodeId(), srcStationDTO.getPosition()));
        trip.setDestination(new Station(dstStationDTO.getNodeId(), dstStationDTO.getPosition()));

        trip = this.tripRepository.save(trip);
        this.artemisService.sendTrip(sessionId, trip);
        this.messinaService.sendBookingRequest(new MessinaBookingRequestDTO(trip.getSource(), trip.getDestination(), trip.getRequestDate()));

        return trip;
    }

    @Transactional
    public ConfirmationDTO checkTripRequest(String sessionId, TripRequestDTO tripRequestDTO) {

        ConfirmationDTO confirmation = null;

        List<Route> routes = this.routeService.getRoutesWithStationIds(List.of(tripRequestDTO.getOsmidSource(), tripRequestDTO.getOsmidDestination()));
        if(routes != null && routes.size() == 1) {
            this.sendRequestToExternal(sessionId, tripRequestDTO);
            confirmation = new ConfirmationDTO(ConfirmationDTO.Status.APPROVED);
        } else {
            Route routeSrc = this.routeService.getRoutesByStationId(tripRequestDTO.getOsmidSource()).get(0);
            Route routeDst = this.routeService.getRoutesByStationId(tripRequestDTO.getOsmidDestination()).get(0);

            if(routeSrc != null && routeDst != null) {
                List<Station> stations = routeSrc.getReachableRoutes().get(routeDst.getName());
                if (stations != null && stations.size() > 0) {
                    if(stations.get(0).getNodeId().equals(tripRequestDTO.getOsmidSource())) {
                        // when start station is identical to first station in map
                        stations.remove(0);
                    }
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
