package it.unisannio.trip.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisannio.trip.dto.TripRequestDTO;

import javax.websocket.*;


public class TripNotificationDecoder implements Decoder.Text<TripRequestDTO> {

    @Override
    public TripRequestDTO decode(String tripString) throws DecodeException {

		ObjectMapper mapper = new ObjectMapper();
		TripRequestDTO tripRequestDTO = null;
		try {
			tripRequestDTO = mapper.readValue(tripString, TripRequestDTO.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return tripRequestDTO;
    }

    @Override
    public boolean willDecode(String tripString) {
        return tripString != null;
    }

    @Override
    public void init(EndpointConfig ec) {
        System.out.println("TripNotificationDecoder - init method called");
    }

    @Override
    public void destroy() {
        System.out.println("TripNotificationDecoder - destroy method called");
    }
}
