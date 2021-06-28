package it.unisannio.trip.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisannio.trip.dto.TripNotificationDTO;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;


public class TripNotificationEncoder implements Encoder.Text<TripNotificationDTO>{

	  @Override
	  public String encode(TripNotificationDTO trip) throws EncodeException {
      
	    String jsonString = null;
		try {  
	      jsonString =  new ObjectMapper().writeValueAsString(trip);
		} catch (Exception e) {}
		return jsonString;
	  }

	  @Override
	  public void init(EndpointConfig ec) {
	    System.out.println("MessageEncoder - init method called");
	  }

	  @Override
	  public void destroy() {
	    System.out.println("MessageEncoder - destroy method called");
	  }
}
