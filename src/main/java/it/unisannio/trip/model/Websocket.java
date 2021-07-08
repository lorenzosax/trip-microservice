package it.unisannio.trip.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Document
public class Websocket implements Serializable {

    @Id
    private String id;
    private String ticket;
    private String instanceSessionId;
    private String instanceTripId;

    public Websocket() {}

    public Websocket(String ticket, String instanceSessionId) {
        this.ticket = ticket;
        this.instanceSessionId = instanceSessionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getInstanceSessionId() {
        return instanceSessionId;
    }

    public void setInstanceSessionId(String instanceSessionId) {
        this.instanceSessionId = instanceSessionId;
    }

    public String getInstanceTripId() {
        return instanceTripId;
    }

    public void setInstanceTripId(String instanceTripId) {
        this.instanceTripId = instanceTripId;
    }
}
