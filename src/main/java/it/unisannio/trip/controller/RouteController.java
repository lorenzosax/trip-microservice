package it.unisannio.trip.controller;

import it.unisannio.trip.dto.RouteDTO;
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
@Path("/routes")
public class RouteController {

    private RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GET
    public Response getStations() {
        List<RouteDTO> routes = this.routeService.getRoutes();
        return Response.ok(routes).build();
    }

    @POST
    public Response insertRoutes(List<RouteDTO> routeList) {
        this.routeService.insertRoutes(routeList);
        return Response.ok().build();
    }
}
