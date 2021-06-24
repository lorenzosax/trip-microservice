package it.unisannio.trip.dto.internal;

import java.io.Serializable;
import java.util.List;

public class Street implements Serializable {

    private int linkId;
    private int from;
    private int to;
    private Double length;
    private int speedlimit;
    private String name;
    private Double weight;
    private Double ffs;
    private List<Coordinate> coordinates;

    public Street() {
    }

    public Street(int linkId, int from, int to, Double length, int speedlimit, String name, Double weight, Double ffs, List<Coordinate> coordinates) {
        this.linkId = linkId;
        this.from = from;
        this.to = to;
        this.length = length;
        this.speedlimit = speedlimit;
        this.name = name;
        this.weight = weight;
        this.ffs = ffs;
        this.coordinates = coordinates;
    }

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public int getSpeedlimit() {
        return speedlimit;
    }

    public void setSpeedlimit(int speedlimit) {
        this.speedlimit = speedlimit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getFfs() {
        return ffs;
    }

    public void setFfs(Double ffs) {
        this.ffs = ffs;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }
}
