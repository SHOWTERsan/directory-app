package ru.showter.directoryappapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionDto {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String notes;
}
