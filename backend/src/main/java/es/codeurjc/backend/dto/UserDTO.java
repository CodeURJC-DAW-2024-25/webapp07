package es.codeurjc.backend.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object (DTO) for User entity.
 * Immutable record to ensure a clean API response.
 */
public record UserDTO(
        Long id,
        String username,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String phoneNumber,
        String address,
        String email,
        List<String> roles,
        boolean banned
) {}