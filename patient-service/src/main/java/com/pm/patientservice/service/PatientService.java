package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailALreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.kafka.KafkaProducer;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    private final BillingServiceGrpcClient billingServiceGrpcClient;

    private final KafkaProducer kafkaProducer;

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toDTO)
                .toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailALreadyExistsException("Patient with email " + patientRequestDTO.getEmail() + " already exists");
        }
        Patient newPatient = patientRepository.save(PatientMapper.toPatient(patientRequestDTO));

        // GRPC request - to billing service to create a new patient account
        billingServiceGrpcClient.createBillingAccount(
                newPatient.getId().toString(),
                newPatient.getName(),
                newPatient.getEmail()
        );

        // send kafka event
        kafkaProducer.sendEvent(newPatient);
        return PatientMapper.toDTO(newPatient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(()
                -> new PatientNotFoundException("Patient with id " + id + " not found"));

        if (patientRequestDTO.getEmail() != null &&
                // checks if another patient with same email and other id.
                // to avoid incorrect update cancellation
                patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw new EmailALreadyExistsException("Patient with email " +
                    patientRequestDTO.getEmail() + " already exists");
        }

        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}





