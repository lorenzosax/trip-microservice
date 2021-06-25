package it.unisannio.trip.controller;

import it.unisannio.trip.dto.StationDTO;
import it.unisannio.trip.dto.TripConfirmDTO;
import it.unisannio.trip.dto.TripRequestDTO;
import it.unisannio.trip.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TripController {

    private TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/stations")
    public ResponseEntity<List<StationDTO>> getStations() {
        List<StationDTO> stations = tripService.getStations();
        return ResponseEntity.ok(stations);
    }

    @PostMapping("/request")
    public ResponseEntity tripRequest(@RequestBody TripRequestDTO requestDTO, @RequestParam("userId") Integer userId) {
        tripService.appendNewRequest(requestDTO, userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/confirm")
    public ResponseEntity confirmTrip(@RequestBody TripConfirmDTO confirmDTO, @RequestParam("userId") Integer userId) {
        tripService.confirmTrip(confirmDTO.getTripId(), userId);
        return ResponseEntity.ok().build();
    }

    /*@GetMapping("/pollingProposal")
    public ResponseEntity<TripDTO> pollingProposal() {

        return ResponseEntity.ok();
    }*/
}
