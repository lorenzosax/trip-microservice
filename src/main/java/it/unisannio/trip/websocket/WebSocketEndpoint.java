package it.unisannio.trip.websocket;

import it.unisannio.trip.dto.TripNotificationDTO;

import javax.inject.Singleton;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.*;

@Singleton
@ServerEndpoint(value = "/ws/notifications/{tripId}", encoders = {TripNotificationEncoder.class})
public class WebSocketEndpoint {
    public static Map<String, Session> peers = Collections.synchronizedMap(new HashMap<String, Session>());

    @OnOpen
    public void start(@PathParam("tripId") String id, Session session) {
        peers.put(id, session);
        System.out.println("A connection has been established");
    }
   
    @OnClose
    public void end(Session session) {
    	peers.remove(session.getId());
    }

    @OnMessage
    public void receive(String message) {
        // in this example we don't receive messages with this endpoint
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
        // to write for handling errors
    }

    public void send(TripNotificationDTO trip) {
        if (peers.get(trip.getTripId()) != null) {
            peers.get(trip.getTripId()).getAsyncRemote().sendObject(trip);
        }
    }
}
