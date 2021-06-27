package it.unisannio.trip.service;

import it.unisannio.trip.dto.StationDTO;
import it.unisannio.trip.model.Station;
import it.unisannio.trip.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StationService {

    private StationRepository stationRepository;

    @Autowired
    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<StationDTO> getStations() {
        List<Station> stations = this.stationRepository.findAll();

        return StationDTO.convert(stations);
    }

    public StationDTO getStationInfo(Integer nodeId) {
        Optional<Station> station = this.stationRepository.findByNodeId(nodeId);
        return station.isPresent() ? StationDTO.convert(station.get()) : null;
    }

    public void insertStations(List<StationDTO> stations) {
        for (StationDTO station: stations) {
            this.stationRepository.insert(new Station(station.getNodeId(), station.getLatitude(), station.getLongitude()));
        }
    }
}
