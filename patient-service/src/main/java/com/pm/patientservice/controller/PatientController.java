package com.pm.patientservice.controller;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.dto.validators.CreatePatientValidationGroup;
import com.pm.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@Tag(name = "Patient", description = "API for managing patients") // then we add @Operation to each func
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    @Operation(summary = "Get all patients")
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        return ResponseEntity.ok().body(patientService.getPatients());
    }

    @PostMapping
    @Operation(summary = "Create patient")
    public ResponseEntity<PatientResponseDTO> createPatient(@Validated({Default.class,
            CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok().body(patientService.createPatient(patientRequestDTO));
    }

    /**
     * now in update, registered date validation is ignored as we are not specifying createdpatient validation group in
     * update patient and only using default.class
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update patient")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id,
                                                            @Validated({Default.class})
                                                            @RequestBody PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok().body(patientService.updatePatient(id, patientRequestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
