package it.unisannio.trip.dto.external;

import it.unisannio.trip.model.Station;

import java.io.Serializable;
import java.util.Date;

public class MessinaBookingRequestDTO implements Serializable {

    private String vid = "GREEN_BUS";
    private int pcount = 1;
    private Station source;
    private Station destination;
    private Date requestDate;

    public MessinaBookingRequestDTO() {}

    public MessinaBookingRequestDTO(Station source, Station destination, Date requestDate) {
        this.source = source;
        this.destination = destination;
        this.requestDate = requestDate;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public int getPcount() {
        return pcount;
    }

    public void setPcount(int pcount) {
        this.pcount = pcount;
    }

    public Station getSource() {
        return source;
    }

    public void setSource(Station source) {
        this.source = source;
    }

    public Station getDestination() {
        return destination;
    }

    public void setDestination(Station destination) {
        this.destination = destination;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }
}
