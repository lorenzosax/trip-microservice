package it.unisannio.trip.dto;

import it.unisannio.trip.model.Route;
import it.unisannio.trip.model.Station;

import java.util.ArrayList;
import java.util.List;

public class RouteDTO {

    private List<StationDTO> stations;

    public RouteDTO() {}

    public RouteDTO(List<StationDTO> stations) {
        this.stations = stations;
    }

    public static List<RouteDTO> convert(List<Route> routeList) {
        List<RouteDTO> result = new ArrayList<>();
        for (Route route : routeList) {
            List<StationDTO> stationList = new ArrayList<>();
            for (Station station : route.getStations()) {
                stationList.add(new StationDTO(station));
            }
            result.add(new RouteDTO(stationList));
        }
        return result;
    }

    public List<StationDTO> getStations() {
        return stations;
    }

    public void setStations(List<StationDTO> stations) {
        this.stations = stations;
    }
}
