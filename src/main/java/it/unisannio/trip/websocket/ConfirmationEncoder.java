package it.unisannio.trip.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisannio.trip.dto.ConfirmationDTO;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;


public class ConfirmationEncoder implements Encoder.Text<ConfirmationDTO>{

	  @Override
	  public String encode(ConfirmationDTO trip) throws EncodeException {
      
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
