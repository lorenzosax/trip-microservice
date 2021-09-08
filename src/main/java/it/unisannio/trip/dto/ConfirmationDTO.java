package it.unisannio.trip.dto;

import it.unisannio.trip.dto.internal.TripDTO;

import java.io.Serializable;
import java.util.List;

public class ConfirmationDTO implements Serializable {

    public enum Status {APPROVED, REJECTED, MULTI_PATHS}

    private Status status;

    private List<TripDTO> trips;

    public ConfirmationDTO() { }

    public ConfirmationDTO(Status status) {
        this.status = status;
    }

    public ConfirmationDTO(Status status, List<TripDTO> trips) {
        this.status = status;
        this.trips = trips;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<TripDTO> getTrips() {
        return trips;
    }

    public void setTrips(List<TripDTO> trips) {
        this.trips = trips;
    }
}
