package es.codeurjc.backend.restController;

import es.codeurjc.backend.dto.BookingDTO;
import es.codeurjc.backend.exception.custom.ResourceNotFoundException;
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
                .orElseThrow(() -> new ResourceNotFoundException("Booking with id " + id + " not found"));
    }

    @Operation(summary = "Create a new booking")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Booking created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "User not authenticated"),
            @ApiResponse(responseCode = "403", description = "User not authorized to create this booking"),
            @ApiResponse(responseCode = "409", description = "User already has an active booking")
    })
    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingDTO dto) {

        Optional<User> currentUserOpt = userService.getAuthenticatedUser();
        if (currentUserOpt.isEmpty()) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        User currentUser = currentUserOpt.get();

        if (!dto.userId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).body("You can only create a booking for your own account");
        }

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
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Booking deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Not allowed to delete this booking"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        Booking booking = bookingService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with id " + id + " not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        boolean isOwner = booking.getUser().getUsername().equals(currentUsername);

        if (!isAdmin && !isOwner) {
            return ResponseEntity.status(403).body("You are not authorized to delete this booking.");
        }

        bookingService.cancelBookingById(id);
        return ResponseEntity.noContent().build();
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
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

}
