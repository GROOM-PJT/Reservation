package com.groomproject.reservationserver.reservation.controller;

import com.groomproject.reservationserver.baseUtil.response.dto.CommonResponse;
import com.groomproject.reservationserver.baseUtil.response.service.ResponseService;
import com.groomproject.reservationserver.reservation.dto.ReservationRequest;
import com.groomproject.reservationserver.reservation.producer.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * @Author : Jeeseob
 * @CreateAt : 2022/10/07
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/reservation")
public class ProducerController {

    private final KafkaService kafkaService;
    private final ResponseService responseService;

    @PostMapping("/add")
    public CommonResponse sendMessage(@RequestBody ReservationRequest requset) {
        kafkaService.sendMessage(requset.toSend());
        log.info(requset.toString());
        return responseService.successResult();
    }
}