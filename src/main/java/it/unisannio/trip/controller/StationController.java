package it.unisannio.trip.controller;

import it.unisannio.trip.dto.StationDTO;
import it.unisannio.trip.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
@Path("/station")
public class StationController {

    private StationService stationService;

    @Autowired
    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GET
    public Response getStations() {
        List<StationDTO> stations = stationService.getStations();
        return Response.ok(stations).build();
    }

    @GET
    @Path("/{id}")
    public Response getStationInfo(@PathParam(value = "id") Integer nodeId) {
        StationDTO stationInfo = stationService.getStationInfo(nodeId);
        return ((stationInfo != null) ? Response.ok(stationInfo) : Response.noContent()).build();
    }

    @POST
    public Response insertStations(List<StationDTO> stationList) {
        stationService.insertStations(stationList);
        return Response.ok().build();
    }
}
