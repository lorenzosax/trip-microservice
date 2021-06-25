package it.unisannio.trip.config;

import it.unisannio.trip.controller.TripController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(TripController.class);
    }
}
