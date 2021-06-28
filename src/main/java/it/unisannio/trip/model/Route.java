package it.unisannio.trip.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;


@Document
public class Route implements Serializable {

    @Id
    private String id;
    private List<Station> stations;

    public Route() {}

    public Route(List<Station> stations) {
        this.stations = stations;
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
}
