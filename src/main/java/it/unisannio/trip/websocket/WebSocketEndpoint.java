package it.unisannio.trip.websocket;

import it.unisannio.trip.dto.ConfirmationDTO;
import it.unisannio.trip.dto.TripRequestDTO;
import it.unisannio.trip.service.TripService;
import it.unisannio.trip.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/api/notifications",
        encoders = {TripNotificationEncoder.class, ConfirmationEncoder.class},
        decoders = {TripNotificationDecoder.class})
public class WebSocketEndpoint {

    private static TripService tripService;

    @Autowired
    public void setTripService(TripService tripService) {
        WebSocketEndpoint.tripService = tripService;
    }

    @OnOpen
    public void start(Session session) {
        WebSocketService.addPeer(session);
        System.out.println("A connection has been established");
    }
   
    @OnClose
    public void end(Session session) {
        WebSocketService.removePeer(session);
    }

    @OnMessage
    public void receive(Session session, TripRequestDTO tripRequestDTO) {
        boolean isFeasibleRequest = tripService.sendRequest(session.getId(), tripRequestDTO);
        if (!isFeasibleRequest) {
            send(session.getId(), new ConfirmationDTO(ConfirmationDTO.Status.REJECT));
        } else {
            send(session.getId(), new ConfirmationDTO(ConfirmationDTO.Status.APPROVED));
        }
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
        // to write for handling errors
    }

    public void send(String sessionId, Object obj) {
        WebSocketService.sendMessage(sessionId, obj);
    }
}
