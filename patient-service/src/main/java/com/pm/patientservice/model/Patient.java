package com.pm.patientservice.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Nonnull
    private String name;

    @Nonnull
    @Email
    @Column(unique = true)
    private String email;

    @Nonnull
    private String address;

    @Nonnull
    private LocalDate dateOfBirth;

    @Nonnull
    private LocalDate registeredDate;

}
