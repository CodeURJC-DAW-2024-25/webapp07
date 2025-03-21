package es.codeurjc.backend.dto;

import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Data Transfer Object representing a user in the system")
public record UserDTO(

        @Schema(description = "Unique identifier of the user", example = "1")
        Long id,

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
        @Schema(description = "Username used for login", example = "johndoe")
        String username,

        @NotBlank(message = "First name is required")
        @Schema(description = "User's first name", example = "John")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Schema(description = "User's last name", example = "Doe")
        String lastName,

        @Past(message = "Date of birth must be in the past")
        @Schema(description = "Date of birth of the user", example = "1990-05-15")
        LocalDate dateOfBirth,

        @Pattern(regexp = "\\+?\\d{9,15}", message = "Phone number must be valid")
        @Schema(description = "Phone number of the user", example = "+34600123456")
        String phoneNumber,

        @Schema(description = "User's address", example = "Calle Falsa 123, Madrid")
        String address,

        @Email(message = "Email must be valid")
        @NotBlank(message = "Email is required")
        @Schema(description = "Email address of the user", example = "john.doe@example.com")
        String email,

        @Schema(description = "Roles assigned to the user", example = "[\"USER\"]")
        List<String> roles,

        @Schema(description = "Indicates if the user is banned", example = "false")
        boolean banned,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must have at least 8 characters")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @Schema(description = "Password for authentication (write-only field)", example = "superSecure123", accessMode = Schema.AccessMode.WRITE_ONLY)
        String password

) {}
