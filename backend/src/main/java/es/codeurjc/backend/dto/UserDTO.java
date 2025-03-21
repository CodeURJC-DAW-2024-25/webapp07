package es.codeurjc.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object (DTO) for User entity.
 * Immutable record to ensure a clean API response.
 */
@Schema(description = "Data Transfer Object representing a user in the system")
public record UserDTO(

        @Schema(description = "Unique identifier of the user", example = "1")
        Long id,

        @Schema(description = "Username used for login", example = "johndoe")
        String username,

        @Schema(description = "User's first name", example = "John")
        String firstName,

        @Schema(description = "User's last name", example = "Doe")
        String lastName,

        @Schema(description = "Date of birth of the user", example = "1990-05-15")
        LocalDate dateOfBirth,

        @Schema(description = "Phone number of the user", example = "+34 600 123 456")
        String phoneNumber,

        @Schema(description = "User's address", example = "Calle Falsa 123, Madrid")
        String address,

        @Schema(description = "Email address of the user", example = "john.doe@example.com")
        String email,

        @Schema(description = "Roles assigned to the user", example = "[\"USER\", \"ADMIN\"]")
        List<String> roles,

        @Schema(description = "Indicates if the user is banned", example = "false")
        boolean banned,

        @Schema(description = "Password for authentication (write-only field)", example = "superSecure123", accessMode = Schema.AccessMode.WRITE_ONLY)
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password

) {}
