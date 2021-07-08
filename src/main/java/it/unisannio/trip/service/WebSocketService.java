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

    public void addPeer(String ticket, Session session) {
        this.websocketRepository.save(new Websocket(ticket, preAppendInstanceToId(session.getId())));
        peers.put(ticket, session);
    }

    public void removePeer(String sessionId) {
        Optional<Websocket> websocket =
                this.websocketRepository.findByInstanceSessionIdAndRemove(preAppendInstanceToId(sessionId));
        websocket.ifPresent(value -> peers.remove(value.getTicket()));
    }

    public void sendMessage(String id, Object msg) throws WebSocketClientNotFoundException {
        Optional<Websocket> websocket =
                this.websocketRepository.findByInstanceTripIdOrInstanceSessionId(preAppendInstanceToId(id));
        if (websocket.isEmpty())
            throw new WebSocketClientNotFoundException("WS identified by instanceTripId/instanceSessionId: <" + id + "> is not present");

        peers.get(websocket.get().getTicket()).getAsyncRemote().sendObject(msg);
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
