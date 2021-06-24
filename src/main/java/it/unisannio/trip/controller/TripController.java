package it.unisannio.trip.controller;

import it.unisannio.trip.dto.StationDTO;
import it.unisannio.trip.dto.TripConfirmDTO;
import it.unisannio.trip.dto.TripRequestDTO;
import it.unisannio.trip.dto.internal.Coordinate;
import it.unisannio.trip.service.TrafficService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TripController {

    private TrafficService trafficService;

    @Autowired
    public TripController(TrafficService trafficService) {
        this.trafficService = trafficService;
    }

    @GetMapping("/stations")
    public ResponseEntity<List<StationDTO>> getStations() {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @PostMapping("/request")
    public ResponseEntity tripRequest(@RequestBody TripRequestDTO requestDTO, @RequestParam("userId") Integer userId) {
        List<Coordinate> shortestPath =
                trafficService.shortestPath(requestDTO.getOsmidSource(), requestDTO.getOsmidDestination());
        // costruire un oggetto che collega user e shortestPath
        // mandare in topic questa richiesta
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/confirm")
    public ResponseEntity confirmTrip(@RequestBody TripConfirmDTO confirmDTO, @RequestParam("userId") Integer userId) {
        // salvare in db la proposta accettata del trip
        return ResponseEntity.ok().build();
    }

}
