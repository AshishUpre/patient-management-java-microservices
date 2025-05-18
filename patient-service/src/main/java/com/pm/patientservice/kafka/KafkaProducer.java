package com.pm.patientservice.kafka;

import com.pm.patientservice.model.Patient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@RequiredArgsConstructor
@Service
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    // telling kafka message - key -> string, value -> byte arr
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendEvent(Patient patient) {
        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("CREATE") // to desc a particular event under a topic (can have categories within topics)
                .build();

        try {
            // first str - topic to send to, second byte arr - message
            // byte arr to keep size down + makes it easy to convert to object in consumer
            kafkaTemplate.send("patient", event.toByteArray());
        } catch (Exception e) {
            log.error("Error while sending event - {} to kafka : {}", event, e.getMessage());
        }
    }
}
