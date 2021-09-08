package it.unisannio.trip.repository;

import it.unisannio.trip.model.Route;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends MongoRepository<Route, String> {

    @Query(value = "{ 'stations.nodeId': { $all: ?0 } }")
    Optional<List<Route>> findByStationIds(List<Integer> ids);

    Optional<Route> findByStationId(Integer id);
}
