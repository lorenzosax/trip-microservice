package it.unisannio.trip.dto.internal;

import java.io.Serializable;

public class StationStatsDTO implements Serializable {
    private int nodeId;
    private int requests;

    public StationStatsDTO() {
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getRequests() {
        return requests;
    }

    public void setRequests(int requests) {
        this.requests = requests;
    }
}
