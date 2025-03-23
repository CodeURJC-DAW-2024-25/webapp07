package es.codeurjc.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO for Restaurant entity")
public record RestaurantDTO(

        @Schema(description = "Unique identifier of the restaurant", example = "1")
        Long id,

        @NotBlank(message = "Location is required")
        @Schema(description = "Location of the restaurant", example = "Calle Uruguay 33, Coslada")
        String location

) {}

