package it.unisannio.trip.dto;

import java.io.Serializable;

public class TripNotifyDTO implements Serializable {

    private Integer tripId;
    private Integer vehicleId;
    private Integer waitTime;
    private Integer tripTimeDuration;

    public TripNotifyDTO(Integer tripId, Integer vehicleId, Integer waitTime, Integer tripTimeDuration) {
        this.tripId = tripId;
        this.vehicleId = vehicleId;
        this.waitTime = waitTime;
        this.tripTimeDuration = tripTimeDuration;
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Integer waitTime) {
        this.waitTime = waitTime;
    }

    public Integer getTripTimeDuration() {
        return tripTimeDuration;
    }

    public void setTripTimeDuration(Integer tripTimeDuration) {
        this.tripTimeDuration = tripTimeDuration;
    }
}
