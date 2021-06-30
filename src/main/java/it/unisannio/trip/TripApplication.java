package it.unisannio.trip;

import it.unisannio.trip.controller.RouteController;
import it.unisannio.trip.controller.StationController;
import it.unisannio.trip.controller.TripController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.ws.rs.ApplicationPath;


@SpringBootApplication
@EnableAsync
@EnableFeignClients
@EnableEurekaClient
@ApplicationPath("api")
public class TripApplication extends ResourceConfig {

	public TripApplication() {
		register(TripController.class);
		register(StationController.class);
		register(RouteController.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(TripApplication.class, args);
	}

}
