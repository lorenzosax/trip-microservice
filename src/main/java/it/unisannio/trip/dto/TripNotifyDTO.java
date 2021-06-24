package it.unisannio.trip.dto;

import java.io.Serializable;

public class TripNotifyDTO implements Serializable {

    private int tripId;
    private int vehicleId;
    private int waitTime;
    private int tripTimeDuration;

    public TripNotifyDTO(int tripId, int vehicleId, int waitTime, int tripTimeDuration) {
        this.tripId = tripId;
        this.vehicleId = vehicleId;
        this.waitTime = waitTime;
        this.tripTimeDuration = tripTimeDuration;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getTripTimeDuration() {
        return tripTimeDuration;
    }

    public void setTripTimeDuration(int tripTimeDuration) {
        this.tripTimeDuration = tripTimeDuration;
    }
}
