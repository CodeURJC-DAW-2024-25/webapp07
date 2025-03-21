package es.codeurjc.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object (DTO) for Order entity.
 * Immutable record to ensure a clean API response.
 */
@Schema(description = "Data Transfer Object representing an order made by a user")
public record OrderDTO(

        @Schema(description = "Unique identifier of the order", example = "123")
        Long id,

        @Schema(description = "List of dishes included in the order")
        List<DishDTO> dishes,

        @Schema(description = "User who placed the order")
        UserDTO user,

        @Schema(description = "Date and time when the order was placed", example = "2024-03-19T15:45:00")
        LocalDateTime orderDate,

        @Schema(description = "Delivery address of the order", example = "123 Main Street, Springfield")
        String address,

        @Schema(description = "Current status of the order", example = "PENDING")
        String status,

        @Schema(description = "Total price of the order", example = "24.99")
        double totalPrice

) {}
