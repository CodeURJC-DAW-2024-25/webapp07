package es.codeurjc.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object representing a restaurant.
 */
@Schema(description = "Data Transfer Object representing a restaurant")
public record RestaurantDTO(

        @Schema(description = "Unique identifier of the restaurant", example = "1")
        Long id,

        @NotBlank(message = "Location is required")
        @Size(min = 15, max = 100, message = "Location must be between 15 and 100 characters, make sure you include the address")
        @Schema(description = "Location of the restaurant", example = "Calle Gran Vía, Madrid, Spain")
        String location

) {}
