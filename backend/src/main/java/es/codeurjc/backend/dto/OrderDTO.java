package es.codeurjc.backend.dto;

import es.codeurjc.backend.dto.DishDTO;
import es.codeurjc.backend.dto.UserDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Data Transfer Object representing an order made by a user")
public record OrderDTO(

        @Schema(description = "Unique identifier of the order", example = "123")
        Long id,

        @NotNull(message = "Dishes list cannot be null")
        @Size(min = 1, message = "Order must contain at least one dish")
        @Schema(description = "List of dishes included in the order")
        List<DishDTO> dishes,

        @NotNull(message = "User is required")
        @Schema(description = "User who placed the order")
        UserDTO user,

        @NotNull(message = "Order date is required")
        @Schema(description = "Date and time when the order was placed", example = "2024-03-19T15:45:00")
        LocalDateTime orderDate,

        @NotBlank(message = "Address cannot be blank")
        @Schema(description = "Delivery address of the order", example = "123 Main Street, Springfield")
        String address,

        @NotBlank(message = "Order status cannot be blank")
        @Schema(description = "Current status of the order", example = "PENDING")
        String status,

        @PositiveOrZero(message = "Total price must be a non-negative value")
        @Schema(description = "Total price of the order", example = "24.99")
        double totalPrice

) {}
