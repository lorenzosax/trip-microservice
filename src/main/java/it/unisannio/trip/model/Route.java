package it.unisannio.trip.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Document
public class Route implements Serializable {

    @Id
    private String id;
    private List<Station> stations;
    private Map<String, List<Station>> reachableRoutes;

    public Route() {}

    public Route(List<Station> stations) {
        this.stations = stations;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        return ((Route) obj).getId().equals(this.id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public void setReachableRoutes(Map<String, List<Station>> reachableRoutes) {
        this.reachableRoutes = reachableRoutes;
    }

    public Map<String, List<Station>> getReachableRoutes() {
        return reachableRoutes;
    }
}
