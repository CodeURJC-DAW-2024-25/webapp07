package es.codeurjc.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object (DTO) for Order entity.
 * Immutable record to ensure a clean API response.
 */
public record OrderDTO(
        Long id,
        List<DishDTO> dishes,
        UserDTO user,
        LocalDateTime orderDate,
        String address,
        String status,
        double totalPrice
) {}
