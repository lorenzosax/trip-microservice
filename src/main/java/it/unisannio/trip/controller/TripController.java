package it.unisannio.trip.controller;

import it.unisannio.trip.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
@Path("/trips")
public class TripController {

    private TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GET
    @Path("/statistics")
    public Response statistics() {
        return Response.ok(this.tripService.getStatistics()).build();
    }

}
