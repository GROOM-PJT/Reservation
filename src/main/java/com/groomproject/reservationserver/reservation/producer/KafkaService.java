package com.groomproject.reservationserver.reservation.producer;

import com.groomproject.reservationserver.reservation.dto.ReservationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * @Author : Jeeseob
 * @CreateAt : 2022/10/07
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {
    private static final String TOPIC = "reservation";
    private final KafkaTemplate<String, ReservationRequest> kafkaTemplate;

    public void sendMessage(ReservationRequest reservationRequest) {
        Message<ReservationRequest> message = MessageBuilder
                .withPayload(reservationRequest)
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .build();

        log.info("Produce message : %s" + reservationRequest.toString());
        kafkaTemplate.send(message);
    }
}