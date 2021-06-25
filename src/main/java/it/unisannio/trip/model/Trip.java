package it.unisannio.trip.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;


@Document
public class Trip implements Serializable {

    public enum Status {
        WAIT, IN_PROGRESS, DONE
    }

    @Id
    private String id;
    private Integer userId;
    private Integer source;
    private Integer destination;
    private Date requestDate = new Date();
    private Integer vehicleId;
    private Status status = Status.WAIT;

    public Trip() {}

    public Trip(String id, Integer userId, Integer source, Integer destination, Date requestDate, Integer vehicleId, Status status) {
        this.id = id;
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.requestDate = requestDate;
        this.vehicleId = vehicleId;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
