package es.codeurjc.backend.restController;


import es.codeurjc.backend.security.jwt.AuthResponse;
import es.codeurjc.backend.security.jwt.LoginRequest;
import es.codeurjc.backend.security.jwt.UserLoginService;
import es.codeurjc.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "User authentication and token management API")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {

    @Autowired
    private UserLoginService userService;

    @Operation(summary = "User login", description = "Authenticates a user and returns an access token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {
        return userService.login(response, loginRequest);
    }

    @Operation(summary = "Refresh access token", description = "Refreshes the access token using the refresh token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue(name = "RefreshToken", required = false) String refreshToken,
            HttpServletResponse response) {
        return userService.refresh(response, refreshToken);
    }

    @Operation(summary = "User logout", description = "Logs out the user and invalidates tokens")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout successful")
    })
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logOut(HttpServletResponse response) {
        return ResponseEntity.ok(new AuthResponse(AuthResponse.Status.SUCCESS, userService.logout(response)));
    }
}
