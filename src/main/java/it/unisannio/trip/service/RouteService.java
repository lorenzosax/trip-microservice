package it.unisannio.trip.service;

import it.unisannio.trip.dto.RouteDTO;
import it.unisannio.trip.dto.StationDTO;
import it.unisannio.trip.model.Route;
import it.unisannio.trip.model.Station;
import it.unisannio.trip.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<RouteDTO> getRoutes() {
        List<Route> routes = this.routeRepository.findAll();
        return RouteDTO.convert(routes);
    }

    public List<Route> getRawRoutes() {
        return this.routeRepository.findAll();
    }

    public List<Route> getRoutesWithStationIds(List<Integer> nodeIds) {
        Optional<List<Route>> routes = this.routeRepository.findByStationIds(nodeIds);
        return routes.orElse(null);
    }

    public List<Route> getRoutesByStationId(Integer stationId) {
        Optional<List<Route>> routes = this.routeRepository.findByStationId(stationId);
        return routes.orElse(null);
    }

    public void insertRoutes(List<RouteDTO> routeList) {
        for (RouteDTO route: routeList) {
            List<Station> stationList = new ArrayList<>();
            for (StationDTO st : route.getStations()) {
                stationList.add(new Station(st.getNodeId(), st.getPosition()));
            }
            this.routeRepository.insert(new Route(route.getName(), stationList, route.getReachableRoutes()));
        }
    }

    public List<StationDTO> getStations() {
        List<RouteDTO> routeList = this.getRoutes();
        List<StationDTO> stationList = new ArrayList<>();
        for (RouteDTO route : routeList) {
            stationList.addAll(route.getStations());
        }
        return stationList.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public StationDTO getStationInfo(Integer nodeId) {
        StationDTO stationDTO = null;
        List<RouteDTO> routeList = this.getRoutes();
        for (RouteDTO route : routeList) {
            for (StationDTO station : route.getStations()) {
                if (station.getNodeId().equals(nodeId)) {
                    stationDTO = station;
                    break;
                }
            }
            if (stationDTO != null) break;
        }
        return stationDTO;
    }
}
