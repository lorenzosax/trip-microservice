package it.unisannio.trip.websocket;

public class WebSocketClientNotFoundException extends Exception {

    public WebSocketClientNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
