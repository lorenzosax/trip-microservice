package it.unisannio.trip.controller;

import it.unisannio.trip.dto.StationDTO;
import it.unisannio.trip.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
@Path("/stations")
public class StationController {

    private RouteService routeService;

    @Autowired
    public StationController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GET
    public Response getStations() {
        List<StationDTO> stations = this.routeService.getStations();
        return Response.ok(stations).build();
    }

    @GET
    @Path("/{id}")
    public Response getStationInfo(@PathParam(value = "id") Integer nodeId) {
        StationDTO stationInfo = this.routeService.getStationInfo(nodeId);
        return ((stationInfo != null) ? Response.ok(stationInfo) : Response.status(Response.Status.NOT_FOUND)).build();
    }
}
