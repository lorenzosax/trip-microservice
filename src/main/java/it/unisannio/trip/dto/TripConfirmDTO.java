package it.unisannio.trip.dto;

import java.io.Serializable;

public class TripConfirmDTO implements Serializable {

    private Integer tripId;

    public TripConfirmDTO() {
    }

    public TripConfirmDTO(Integer tripId) {
        this.tripId = tripId;
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }
}
