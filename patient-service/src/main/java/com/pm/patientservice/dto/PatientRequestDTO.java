package com.pm.patientservice.dto;

import com.pm.patientservice.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatientRequestDTO {
    @NotBlank
    // spring auto returns the message when validation fails -> this is part of validation package
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank
    @Email(message = "Email is required")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Date of birth is required")
    private String dateOfBirth;

    // adding it to CreatePatientValidationGroup, so it will only be validated only when @Validated({Default.class, CreatePatientValidationGroup.class})
    // is used
    @NotBlank(groups = CreatePatientValidationGroup.class, message = "Registered date is required")
    private String registeredDate;
}
