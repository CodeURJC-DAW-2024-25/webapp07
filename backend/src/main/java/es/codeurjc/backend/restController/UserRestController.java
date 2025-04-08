package es.codeurjc.backend.restController;

import es.codeurjc.backend.dto.UserDTO;
import es.codeurjc.backend.exception.custom.ResourceNotFoundException;
import es.codeurjc.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import io.swagger.v3.oas.annotations.Parameter;


@Tag(name = "Users", description = "Operations related to user management")
@RestController
@CrossOrigin(methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.PATCH })
@RequestMapping("/api/v1/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Get all users", description = "Retrieve a list of all users. Optionally filter by username or email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @Parameter(description = "Query to search by username or email", required = false)
            @RequestParam(required = false) String query) {

        List<UserDTO> users = (query != null && !query.isEmpty())
                ? userService.searchUsers(query)
                : userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "ID of the user to retrieve") @PathVariable Long id) {

        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    @Operation(summary = "Get authenticated user", description = "Retrieve the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated user found"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getAuthenticatedUser() {
        return userService.getAuthenticatedUserDto()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(401).build());
    }

    @Operation(summary = "Register new user", description = "Create a new user with the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/new")
    public ResponseEntity<UserDTO> createUser(
            @Parameter(description = "User data to create", required = true) @Valid @RequestBody UserDTO userDTO) {

        Long userId = userService.registerUser(userDTO);
        URI location = URI.create("/api/v1/users/" + userId);
        return ResponseEntity.created(location).body(userDTO);
    }

    @Operation(summary = "Update existing user", description = "Update user details for the given user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "ID of the user to update") @PathVariable Long id,
            @Parameter(description = "Updated user data") @RequestBody UserDTO userDTO) {

        Optional<UserDTO> updatedUser = userService.updateUser(id, userDTO);
        return updatedUser.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO> editAuthenticatedUser(
            @Parameter(description = "Updated user data") @RequestBody UserDTO userDTO) {
        Optional<UserDTO> updatedUser = userService.updateUser(userDTO.id(), userDTO);
        return updatedUser.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete user", description = "Delete the user with the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete") @PathVariable Long id) {

        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Ban user", description = "Mark the specified user as banned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User banned successfully")
    })
    @PatchMapping("/{id}/banned")
    public ResponseEntity<Void> banUser(
            @Parameter(description = "ID of the user to ban") @PathVariable Long id) {

        userService.banUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Unban user", description = "Remove the ban from the specified user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User unbanned successfully")
    })
    @PatchMapping("/{id}/unbanned")
    public ResponseEntity<Void> unbanUser(
            @Parameter(description = "ID of the user to unban") @PathVariable Long id) {

        userService.unbanUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search users", description = "Search for users by matching username or email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    @GetMapping("/found")
    public ResponseEntity<List<UserDTO>> searchUsers(
            @Parameter(description = "Search query for username or email") @RequestParam String query) {

        List<UserDTO> users = userService.searchUsers(query);
        return ResponseEntity.ok(users);
    }
}
