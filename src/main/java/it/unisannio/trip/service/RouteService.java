package it.unisannio.trip.service;

import it.unisannio.trip.dto.RouteDTO;
import it.unisannio.trip.dto.StationDTO;
import it.unisannio.trip.model.Route;
import it.unisannio.trip.model.Station;
import it.unisannio.trip.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RouteService {

    private RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Cacheable("routes")
    public List<RouteDTO> getRoutes() {
        List<Route> routes = this.routeRepository.findAll();
        return RouteDTO.convert(routes);
    }

    @Cacheable("rawRoutes")
    public List<Route> getRawRoutes() {
        return this.routeRepository.findAll();
    }

    public List<Route> getRoutesWithStationIds(List<Integer> nodeIds) {
        Optional<List<Route>> routes = this.routeRepository.findByStationIds(nodeIds);
        return routes.orElse(null);
    }

    public void insertRoutes(List<RouteDTO> routeList) {
        for (RouteDTO route: routeList) {
            List<Station> stationList = new ArrayList<>();
            for (StationDTO st : route.getStations()) {
                stationList.add(new Station(st.getNodeId(), st.getPosition()));
            }
            this.routeRepository.insert(new Route(stationList));
        }
    }
}
