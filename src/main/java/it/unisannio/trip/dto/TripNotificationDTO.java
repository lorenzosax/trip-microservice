package it.unisannio.trip.dto;

import java.io.Serializable;

public class TripNotificationDTO implements Serializable {

    private String tripId;
    private String vehicleId;
    private Integer pickUpNodeId;

    public TripNotificationDTO() { }

    public TripNotificationDTO(String tripId, String vehicleId, Integer pickUpNodeId) {
        this.tripId = tripId;
        this.vehicleId = vehicleId;
        this.pickUpNodeId = pickUpNodeId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getPickUpNodeId() {
        return pickUpNodeId;
    }

    public void setPickUpNodeId(Integer pickUpNodeId) {
        this.pickUpNodeId = pickUpNodeId;
    }
}
