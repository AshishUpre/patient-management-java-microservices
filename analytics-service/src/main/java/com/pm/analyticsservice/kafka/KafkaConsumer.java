package com.pm.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    // If we see in producer, event is sent after conv to byte arr. Group id tells kafka broker who this consumer is
    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumeEvent(byte[] event) {
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            log.info("Received patient event : [patientid = {}, patient name = {}, patient email = {}]",
                    patientEvent.getPatientId(), patientEvent.getName(), patientEvent.getEmail());
        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing event : {}", e.getMessage());
            // throw new RuntimeException("Error while parsing event from byte array to object : " + e.getMessage());
        }

    }
}
