package it.unisannio.trip.websocket;

import it.unisannio.trip.dto.ConfirmationDTO;
import it.unisannio.trip.dto.TripRequestDTO;
import it.unisannio.trip.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.*;

@ServerEndpoint(value = "/api/notifications",
        encoders = {TripNotificationEncoder.class, ConfirmationEncoder.class},
        decoders = {TripNotificationDecoder.class})
public class WebSocketEndpoint {
    public static List<Session> peers = new ArrayList<>();

    private static TripService tripService;

    @Autowired
    public void setTripService(TripService tripService) {
        WebSocketEndpoint.tripService = tripService;
    }

    @OnOpen
    public void start(Session session) {
        peers.add(session);
        System.out.println("A connection has been established");
    }
   
    @OnClose
    public void end(Session session) {
    	peers.remove(session);
    }

    @OnMessage
    public void receive(Session session, TripRequestDTO tripRequestDTO) {
        boolean isFeasibleRequest = tripService.appendNewRequest(tripRequestDTO);
        if (!isFeasibleRequest) {
            send(session, new ConfirmationDTO(ConfirmationDTO.Status.REJECT));
        } else {
            send(session, new ConfirmationDTO(ConfirmationDTO.Status.APPROVED));
        }
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
        // to write for handling errors
    }

    public void send(Session session, Object obj) {
        session.getAsyncRemote().sendObject(obj);
    }
}
