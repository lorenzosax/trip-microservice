package it.unisannio.trip.dto;

import java.io.Serializable;

public class ConfirmationDTO implements Serializable {

    public enum Status {APPROVED, REJECT}

    private Status status;

    public ConfirmationDTO() { }

    public ConfirmationDTO(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
