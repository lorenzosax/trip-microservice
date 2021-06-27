package it.unisannio.trip.dto;

import it.unisannio.trip.model.Station;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StationDTO implements Serializable {

    private Integer nodeId;
    private Double latitude;
    private Double longitude;

    public StationDTO() {
    }

    public StationDTO(Integer nodeId, Double latitude, Double longitude) {
        this.nodeId = nodeId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static List<StationDTO> convert(List<Station> stationList) {
        List<StationDTO> result = new ArrayList<>();
        for (Station station : stationList) {
            result.add(new StationDTO(station.getNodeId(), station.getLatitude(), station.getLongitude()));
        }
        return result;
    }

    public static StationDTO convert(Station station) {
        return new StationDTO(station.getNodeId(), station.getLatitude(), station.getLongitude());
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
