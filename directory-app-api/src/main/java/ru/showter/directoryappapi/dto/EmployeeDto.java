package ru.showter.directoryappapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private Long id;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Profession is required")
    private Long professionId;

    private String professionName;

    @NotNull(message = "Department is required")
    private Long departmentId;

    private String departmentName;

    private String notes;
}
