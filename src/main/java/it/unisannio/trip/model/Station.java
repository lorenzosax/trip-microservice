package it.unisannio.trip.model;

import it.unisannio.trip.dto.internal.Coordinate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Document
public class Station implements Serializable {

    @Id
    private String id;
    private Integer nodeId;
    private Coordinate position;

    public Station() {}

    public Station(Integer nodeId, Coordinate position) {
        this.nodeId = nodeId;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }
}
