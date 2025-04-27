package es.codeurjc.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for the {@link es.codeurjc.backend.model.Booking} entity.
 * This DTO is used to transfer booking data between layers.
 */
@Schema(description = "Data Transfer Object for Booking entity")
public record BookingDTO(

        /**
         * Unique identifier of the booking.
         */
        @Schema(description = "Unique identifier of the booking", example = "1")
        Long id,

        /**
         * ID of the associated restaurant.
         */
        @NotNull
        @Schema(description = "ID of the restaurant", example = "3")
        Long restaurantId,

        /**
         * ID of the user who made the booking.
         */
        @NotNull
        @Schema(description = "ID of the user", example = "5")
        Long userId,

        /**
         * Date of the booking.
         */
        @NotNull
        @Schema(description = "Booking date", example = "2025-04-12")
        LocalDate date,

        /**
         * Shift of the booking (e.g. LUNCH or DINNER).
         */
        @NotBlank
        @Schema(description = "Shift for the booking", example = "LUNCH or DINNER")
        String shift,

        /**
         * Number of people included in the booking.
         */
        @Min(1)
        @Max(40)
        @Schema(description = "Number of people", example = "4")
        int numPeople,

        @Schema(description = "First name of the person booking", example = "John")
        String firstName,

        @Schema(description = "Last name of the person booking", example = "Doe")
        String lastName,

        @Schema(description = "Phone number provided for the booking", example = "123456789")
        String phone,

        @Schema(description = "Email provided for the booking", example = "john@example.com")
        String email,

        @Schema(description = "Location of the restaurant", example = "Calle Gran Vía 10 Madrid")
        String restaurantLocation


) {}
