package it.unisannio.trip.websocket;

import it.unisannio.trip.dto.TripNotifyDTO;

import javax.inject.Singleton;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.*;

@Singleton
@ServerEndpoint(value = "/ws/notifications", encoders = {TripNotificationEncoder.class})
public class WebSocketEndpoint {
    public static Map<String, Session> peers = Collections.synchronizedMap(new HashMap<String, Session>());

    @OnOpen
    public void start(Session session) {
        peers.put(session.getId(), session);
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

    public void send(TripNotifyDTO trip, String sessionId) {
        if (peers.get(sessionId) != null) {
            peers.get(sessionId).getAsyncRemote().sendObject(trip);
        }
    }
}
