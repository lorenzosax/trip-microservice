package it.unisannio.trip.repository;

import it.unisannio.trip.model.Websocket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebsocketRepository extends MongoRepository<Websocket, String> {

    @Query(value = "{ instanceSessionId: ?0 }", delete = true)
    Optional<Websocket> findByInstanceSessionIdAndRemove(String sessionId);

    @Query(value = "{ $or: [ { instanceTripId: ?0 }, { instanceSessionId: ?0 } ] }")
    Optional<Websocket> findByInstanceTripIdOrInstanceSessionId(String tripId);

    Optional<Websocket> findByInstanceSessionId(String instanceSessionId);
}
