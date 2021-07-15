package it.unisannio.trip.dto;

import it.unisannio.trip.dto.internal.Coordinate;
import it.unisannio.trip.model.Station;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StationDTO implements Serializable {

    private Integer nodeId;
    private Coordinate position;

    public StationDTO() { }

    public StationDTO(Station station) {
        this.nodeId = station.getNodeId();
        this.position = station.getPosition();
    }

    public StationDTO(Integer nodeId, Coordinate position) {
        this.nodeId = nodeId;
        this.position = position;
    }

    public static List<StationDTO> convert(List<Station> stationList) {
        List<StationDTO> result = new ArrayList<>();
        for (Station station : stationList) {
            result.add(new StationDTO(station));
        }
        return result;
    }

    public static StationDTO convert(Station station) {
        return new StationDTO(station);
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }
}
