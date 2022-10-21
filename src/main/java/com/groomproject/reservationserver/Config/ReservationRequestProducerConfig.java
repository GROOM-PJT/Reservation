package com.groomproject.reservationserver.Config;

import com.groomproject.reservationserver.reservation.dto.ReservationRequest;
import com.groomproject.reservationserver.reservation.dto.ReservationRequsetToSend;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author : Jeeseob
 * @CreateAt : 2022/10/07
 */

@Configuration
public class ReservationRequestProducerConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String servers;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, ReservationRequsetToSend> reservationRequestProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, ReservationRequsetToSend> reservationRequestKafkaTemplate() {
        return new KafkaTemplate<>(reservationRequestProducerFactory());
    }
}
