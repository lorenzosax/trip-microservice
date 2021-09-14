package it.unisannio.trip.service;

import it.unisannio.trip.dto.external.MessinaBookingRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(name = "messinaServiceFeignClient", url = "${api.external.prediction.base-url}")
public interface MessinaService {

    @PostMapping("/insertBookingDocument")
    void sendBookingRequest(MessinaBookingRequestDTO request);

}
