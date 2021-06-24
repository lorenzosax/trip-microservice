package it.unisannio.trip.dto;

import java.io.Serializable;

public class TripRequestDTO implements Serializable {

    private int osmidSource;
    private int osmidDestination;

    public TripRequestDTO() {
    }

    public TripRequestDTO(int osmidSource, int osmidDestination) {
        this.osmidSource = osmidSource;
        this.osmidDestination = osmidDestination;
    }

    public int getOsmidSource() {
        return osmidSource;
    }

    public void setOsmidSource(int osmidSource) {
        this.osmidSource = osmidSource;
    }

    public int getOsmidDestination() {
        return osmidDestination;
    }

    public void setOsmidDestination(int osmidDestination) {
        this.osmidDestination = osmidDestination;
    }
}
