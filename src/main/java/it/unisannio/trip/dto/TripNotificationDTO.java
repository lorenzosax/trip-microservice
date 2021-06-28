package it.unisannio.trip.dto;

import java.io.Serializable;

public class TripNotificationDTO implements Serializable {

    private String tripId;
    private Integer vehicleId;
    private Integer pickNodeUpId;

    public TripNotificationDTO(String tripId, Integer vehicleId, Integer pickNodeUpId) {
        this.tripId = tripId;
        this.vehicleId = vehicleId;
        this.pickNodeUpId = pickNodeUpId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getPickNodeUpId() {
        return pickNodeUpId;
    }

    public void setPickNodeUpId(Integer pickNodeUpId) {
        this.pickNodeUpId = pickNodeUpId;
    }
}
