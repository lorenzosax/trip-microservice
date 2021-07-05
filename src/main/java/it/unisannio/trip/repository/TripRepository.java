package it.unisannio.trip.repository;

import it.unisannio.trip.model.Trip;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TripRepository extends MongoRepository<Trip, String> {

    int countBySourceAndRequestDateIsGreaterThan(int sourceId, Date date);
}
