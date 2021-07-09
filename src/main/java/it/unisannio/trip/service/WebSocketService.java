package it.unisannio.trip.service;

import it.unisannio.trip.model.Websocket;
import it.unisannio.trip.repository.WebsocketRepository;
import it.unisannio.trip.websocket.WebSocketClientNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WebSocketService {

    @Value("${server.instance-id}")
    private String instanceId;

    private static Map<String, Session> peers = new HashMap<>();

    private WebsocketRepository websocketRepository;

    @Autowired
    public WebSocketService(WebsocketRepository websocketRepository) {
        this.websocketRepository = websocketRepository;
    }

    public void addPeer(Session session) {
        this.websocketRepository.save(new Websocket(preAppendInstanceToId(session.getId())));
        peers.put(preAppendInstanceToId(session.getId()), session);
    }

    public void removePeer(String sessionId) {
        Optional<Websocket> websocket =
                this.websocketRepository.findByInstanceSessionIdAndRemove(preAppendInstanceToId(sessionId));
        websocket.ifPresent(value -> peers.remove(value.getInstanceSessionId()));
    }

    public void sendMessage(String tripId, Object msg) throws WebSocketClientNotFoundException {
        Optional<Websocket> websocket =
                this.websocketRepository.findByInstanceTripId(preAppendInstanceToId(tripId));
        if (websocket.isEmpty())
            throw new WebSocketClientNotFoundException("WS identified by tripId: <" + tripId + "> is not present");

        peers.get(websocket.get().getInstanceSessionId()).getAsyncRemote().sendObject(msg);
    }

    public void addRequestTripId(String sessionId, String tripId) {
        Optional<Websocket> websocket =
                this.websocketRepository.findByInstanceSessionId(preAppendInstanceToId(sessionId));
        if (websocket.isPresent()) {
            websocket.get().setInstanceTripId(preAppendInstanceToId(tripId));
            this.websocketRepository.save(websocket.get());
        }
    }

    private String preAppendInstanceToId(String id) {
        return instanceId.concat("@").concat(id);
    }
}
