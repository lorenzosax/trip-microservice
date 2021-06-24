package it.unisannio.trip.dto;

import java.io.Serializable;

public class TripConfirmDTO implements Serializable {

    private int tripId;

    public TripConfirmDTO() {
    }

    public TripConfirmDTO(int tripId) {
        this.tripId = tripId;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }
}
