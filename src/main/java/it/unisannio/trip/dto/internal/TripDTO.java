package it.unisannio.trip.dto.internal;

import java.io.Serializable;
import java.util.Date;


public class TripDTO implements Serializable {

    private static final long serialVersionUID = 6794687963821239314L;

    private String id;
    private Integer source;
    private Integer destination;
    private Date requestDate;

    public TripDTO() {}

    public TripDTO(String id, Integer source, Integer destination, Date requestDate, Integer vehicleId) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.requestDate = requestDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getDestination() {
        return destination;
    }

    public void setDestination(Integer destination) {
        this.destination = destination;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }
}
