package es.codeurjc.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Data Transfer Object for Booking entity")
public record BookingDTO(
        @Schema(description = "Unique identifier of the booking", example = "1")
        Long id,

        @NotNull
        @Schema(description = "ID of the restaurant", example = "3")
        Long restaurantId,

        @NotNull
        @Schema(description = "ID of the user", example = "5")
        Long userId,

        @NotNull
        @Schema(description = "Booking date", example = "2025-04-12")
        LocalDate date,

        @NotBlank
        @Schema(description = "Shift for the booking", example = "LUNCH or DINNER")
        String shift,

        @Min(1)
        @Max(40)
        @Schema(description = "Number of people", example = "4")
        int numPeople
) {}

