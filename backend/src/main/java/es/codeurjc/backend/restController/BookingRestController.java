package es.codeurjc.backend.restController;

import es.codeurjc.backend.dto.BookingDTO;
import es.codeurjc.backend.mapper.BookingMapper;
import es.codeurjc.backend.model.Booking;
import es.codeurjc.backend.service.BookingService;
import es.codeurjc.backend.service.RestaurantService;
import es.codeurjc.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import es.codeurjc.backend.model.User;


import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@Tag(name = "Bookings", description = "Booking management REST API")
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingRestController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @Operation(summary = "Get all bookings")
    @GetMapping
    public List<BookingDTO> findAll() {
        return bookingService.getAllBookings().stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Operation(summary = "Get a booking by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Booking found"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable Long id) {
        return bookingService.findById(id)
                .map(bookingMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new booking")
    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingDTO dto) {

        // Get authenticated user
        Optional<User> currentUserOpt = userService.getAuthenticatedUser();
        if (currentUserOpt.isEmpty()) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        User currentUser = currentUserOpt.get();

        // Only allow booking for the authenticated user
        if (!dto.userId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).body("You can only create a booking for your own account");
        }

        // Check if user already has an active booking
        Optional<Booking> activeBooking = bookingService.findActiveBookingByUser(currentUser);
        if (activeBooking.isPresent()) {
            return ResponseEntity.status(409).body("You already have an active booking. Please cancel it before creating a new one.");
        }

        Booking booking = bookingMapper.toEntity(dto);
        bookingService.save(booking);

        URI location = fromCurrentRequest().path("/{id}")
                .buildAndExpand(booking.getId()).toUri();

        return ResponseEntity.created(location).body(bookingMapper.toDto(booking));
    }



    @Operation(summary = "Delete a booking")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        Optional<Booking> optionalBooking = bookingService.findById(id);
        if (optionalBooking.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Booking booking = optionalBooking.get();

        // Get the current authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get current username (assuming your UserDetails returns it)
        String currentUsername = authentication.getName();

        // Check if the user is ADMIN
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        // Check if the booking belongs to the current user
        boolean isOwner = booking.getUser().getUsername().equals(currentUsername);

        if (!isAdmin && !isOwner) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        bookingService.cancelBookingById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a booking")
    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable Long id, @Valid @RequestBody BookingDTO dto) {
        Optional<Booking> existing = bookingService.findById(id);
        if (existing.isEmpty()) return ResponseEntity.notFound().build();

        Booking booking = bookingMapper.toEntity(dto);
        booking.setId(id);
        bookingService.save(booking);

        return ResponseEntity.ok(bookingMapper.toDto(booking));
    }
    @Operation(summary = "Get the current user's active booking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Booking found"),
            @ApiResponse(responseCode = "404", description = "No active booking found for the user")
    })
    @GetMapping("/me")
    public ResponseEntity<BookingDTO> getAuthenticatedUserBooking() {
        return userService.getAuthenticatedUser()
                .flatMap(bookingService::findActiveBookingByUser)
                .map(bookingMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).build());
    }
}
