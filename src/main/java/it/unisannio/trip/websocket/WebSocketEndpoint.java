package it.unisannio.trip.websocket;

import it.unisannio.trip.Utils;
import it.unisannio.trip.dto.ConfirmationDTO;
import it.unisannio.trip.dto.TripRequestDTO;
import it.unisannio.trip.model.Trip;
import it.unisannio.trip.service.TripService;
import it.unisannio.trip.service.WebSocketService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;

@ServerEndpoint(value = "/api/notifications",
        encoders = {TripNotificationEncoder.class, ConfirmationEncoder.class},
        decoders = {TripNotificationDecoder.class})
public class WebSocketEndpoint {

    private final Logger logger = LoggerFactory.getLogger(WebSocketEndpoint.class);
    private static TripService tripService;
    private static WebSocketService webSocketService;

    @Autowired
    public void setTripService(TripService tripService) {
        WebSocketEndpoint.tripService = tripService;
    }

    @Autowired
    public void setWebSocketService(WebSocketService webSocketService) {
        WebSocketEndpoint.webSocketService = webSocketService;
    }

    @OnOpen
    public void start(Session session) throws Exception {
        Map<String, String> queryMap = Utils.buildQueryMap(session.getQueryString());
        if (queryMap != null && queryMap.get("ticket") != null) {
            webSocketService.addPeer(queryMap.get("ticket"), session);
            logger.info("A WS connection has been established");
        } else
            throw new Exception();
    }
   
    @OnClose
    public void end(Session session) {
        webSocketService.removePeer(session.getId());
    }

    @OnMessage
    public void receive(Session session, TripRequestDTO tripRequestDTO) {
        Trip tripRequest = tripService.sendRequestToMOM(session.getId(), tripRequestDTO);
        ConfirmationDTO confirmation;
        try {
            if (tripRequest == null) {
                confirmation = new ConfirmationDTO(ConfirmationDTO.Status.REJECTED);
            } else {
                confirmation = new ConfirmationDTO(ConfirmationDTO.Status.APPROVED);
            }
            webSocketService.sendMessage(session.getId(), confirmation);
        } catch (WebSocketClientNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
        // to write for handling errors
    }
}
