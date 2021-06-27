package it.unisannio.trip.controller;

import it.unisannio.trip.dto.StationDTO;
import it.unisannio.trip.dto.TripConfirmDTO;
import it.unisannio.trip.dto.TripRequestDTO;
import it.unisannio.trip.service.StationService;
import it.unisannio.trip.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
@Path("/trip")
public class TripController {

    private TripService tripService;


    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @POST
    @Path("/request")
    public Response tripRequest(TripRequestDTO requestDTO) {
        Integer userId = 2;
        tripService.appendNewRequest(requestDTO, userId);
        return Response.accepted().build();
    }

    @POST
    @Path("/confirm")
    public Response confirmTrip(TripConfirmDTO confirmDTO) {
        Integer userId = 2;
        tripService.confirmTrip(confirmDTO.getTripId(), userId);
        return Response.ok().build();
    }

}
