package it.unisannio.trip.service;

import it.unisannio.trip.model.Websocket;
import it.unisannio.trip.repository.WebsocketRepository;
import it.unisannio.trip.websocket.WebSocketClientNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.Session;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WebSocketService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);

    private static final String instanceId = initializeInstanceId();

    private static Map<String, Session> peers = new HashMap<>();

    private WebsocketRepository websocketRepository;

    @Autowired
    public WebSocketService(WebsocketRepository websocketRepository) {
        this.websocketRepository = websocketRepository;
    }

    public void addPeer(Session session) {
        String id = preAppendInstanceToId(session.getId());
        this.websocketRepository.save(new Websocket(id));
        peers.put(id, session);
        logger.info("New websocket opened: " + id);
    }

    public void removePeer(String sessionId) {
        Optional<Websocket> websocket =
                this.websocketRepository.findByInstanceSessionIdAndRemove(preAppendInstanceToId(sessionId));
        websocket.ifPresent(value -> {
            peers.remove(value.getInstanceSessionId());
            logger.info("Websocket removed: " + value.getInstanceSessionId());
        });
    }

    public void sendMessage(String tripId, Object msg) throws WebSocketClientNotFoundException {
        Optional<Websocket> websocket =
                this.websocketRepository.findByInstanceTripId(preAppendInstanceToId(tripId));
        if (websocket.isEmpty())
            throw new WebSocketClientNotFoundException("WS identified by tripId: <" + tripId + "> is not present");

        peers.get(websocket.get().getInstanceSessionId()).getAsyncRemote().sendObject(msg);
        logger.info("Message sent to websocket: " + websocket.get().getInstanceSessionId());
    }

    @Transactional
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

    private static String initializeInstanceId() {
        Timestamp t = new Timestamp(System.currentTimeMillis());
        String instanceId = String.valueOf(t.getTime());
        logger.info("Instance ID: " + instanceId);
        return instanceId;
    }
}
