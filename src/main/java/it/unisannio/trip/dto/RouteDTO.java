package it.unisannio.trip.dto;

import it.unisannio.trip.model.Route;
import it.unisannio.trip.model.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteDTO {

    private String id;
    private List<StationDTO> stations;
    private Map<String, List<Station>> reachableRoutes;

    public RouteDTO() {}

    public RouteDTO(String id, List<StationDTO> stations, Map<String, List<Station>> reachableRoutes) {
        this.id = id;
        this.stations = stations;
        this.reachableRoutes = reachableRoutes;
    }

    public static List<RouteDTO> convert(List<Route> routeList) {
        List<RouteDTO> result = new ArrayList<>();
        for (Route route : routeList) {
            List<StationDTO> stationList = new ArrayList<>();
            for (Station station : route.getStations()) {
                stationList.add(new StationDTO(station));
            }
            result.add(new RouteDTO(route.getId(), stationList, route.getReachableRoutes()));
        }
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<StationDTO> getStations() {
        return stations;
    }

    public void setStations(List<StationDTO> stations) {
        this.stations = stations;
    }

    public Map<String, List<Station>> getReachableRoutes() {
        return reachableRoutes;
    }

    public void setReachableRoutes(Map<String, List<Station>> reachableRoutes) {
        this.reachableRoutes = reachableRoutes;
    }
}
