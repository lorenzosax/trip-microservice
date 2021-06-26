package it.unisannio.trip;

import it.unisannio.trip.controller.TripController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableFeignClients
public class TripApplication extends ResourceConfig {

	public TripApplication() {
		register(TripController.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(TripApplication.class, args);
	}

}
