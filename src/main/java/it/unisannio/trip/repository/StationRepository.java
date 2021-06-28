package it.unisannio.trip.repository;

import it.unisannio.trip.model.Station;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StationRepository extends MongoRepository<Station, String> {

    public Optional<Station> findByNodeId(Integer nodeId);
}
