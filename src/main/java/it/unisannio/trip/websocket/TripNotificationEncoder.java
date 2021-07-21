package it.unisannio.trip.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
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
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonString;
	  }

	  @Override
	  public void init(EndpointConfig ec) { }

	  @Override
	  public void destroy() { }
}
