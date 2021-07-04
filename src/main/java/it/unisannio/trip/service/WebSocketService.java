package it.unisannio.trip.service;

import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;


@Service
public final class WebSocketService {

    private static Map<String, Session> peers = new HashMap<>();

    public WebSocketService() { }

    public static Session getPeer(String sessionId) {
        return peers.get(sessionId);
    }

    public static void addPeer(Session session) {
        peers.put(session.getId(), session);
    }

    public static void removePeer(Session session) {
        peers.remove(session.getId());
    }

    public static void sendMessage(String sessionId, Object msg) {
        peers.get(sessionId).getAsyncRemote().sendObject(msg);
    }
}
