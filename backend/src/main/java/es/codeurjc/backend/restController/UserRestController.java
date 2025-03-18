package es.codeurjc.backend.restController;

import es.codeurjc.backend.dto.UserDTO;
import es.codeurjc.backend.mapper.UserMapper;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Tag(name = "Users", description = "User management REST API")
@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Operation(summary = "Get all users", description = "Returns a list of all registered users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        System.out.println("🔍 Usuarios de la BD antes del mapeo: " + users);

        List<UserDTO> usersDTO = users.stream()
                .map(userMapper::toDto)
                .toList();

        System.out.println("🔍 Usuarios después del mapeo a DTO: " + usersDTO);
        return ResponseEntity.ok(usersDTO);
    }


    @Operation(summary = "Get a user by ID", description = "Fetches a user by their unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get the authenticated user", description = "Returns the currently logged-in user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authenticated user retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getAuthenticatedUser() {
        return userService.getAuthenticatedUser()
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(401).build());
    }

    @Operation(summary = "Register a new user", description = "Creates a new user and returns the created user with ID")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        userService.registerUser(user);

        URI location = URI.create("/api/v1/users/" + user.getId());
        return ResponseEntity.created(location).body(userMapper.toDto(user));
    }

    @Operation(summary = "Update user information", description = "Updates the details of an existing user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        Optional<User> existingUser = userService.findById(id);

        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userMapper.toEntity(userDTO);
        user.setId(id);
        userService.updateUser(user);

        return ResponseEntity.ok(userMapper.toDto(user));
    }
    @Operation(summary = "Delete a user", description = "Permanently deletes a user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Ban a user", description = "Marks a user as banned in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User banned successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/banned")
    public ResponseEntity<Void> banUser(@PathVariable Long id) {
        userService.banUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Unban a user", description = "Removes the banned status from a user")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User unbanned successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/unbanned")
    public ResponseEntity<Void> unbanUser(@PathVariable Long id) {
        userService.unbanUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search users", description = "Searches users by username or email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search results returned",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String query) {
        List<UserDTO> users = userService.searchUsers(query)
                .stream()
                .map(userMapper::toDto)
                .toList();
        return ResponseEntity.ok(users);
    }
}
