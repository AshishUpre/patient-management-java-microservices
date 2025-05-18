package com.pm.patientservice.dto;

import lombok.Data;

/**
 * here everything is used as string, so no issue when converting to json in http response
 */
@Data
public class PatientResponseDTO {
    private String id;
    private String name;
    private String email;
    private String address;
    private String dateOfBirth;
}
