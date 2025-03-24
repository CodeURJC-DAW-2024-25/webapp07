package es.codeurjc.backend.restController;

import es.codeurjc.backend.dto.BookingDTO;
import es.codeurjc.backend.exception.custom.ResourceNotFoundException;
import es.codeurjc.backend.mapper.BookingMapper;
import es.codeurjc.backend.model.Booking;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.BookingService;
import es.codeurjc.backend.service.RestaurantService;
import es.codeurjc.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

/**
 * REST controller for managing bookings.
 */
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

    /**
     * Retrieves all active bookings.
     *
     * @return List of all active bookings.
     */
    @Operation(summary = "Get all bookings")
    @GetMapping
    public List<BookingDTO> findAll() {
        return bookingService.getAllBookings().stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param id The ID of the booking.
     * @return The booking if found, 404 otherwise.
     */
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

    /**
     * Creates a new booking for the authenticated user.
     *
     * @param dto Booking data.
     * @return 201 if created, 401/403/409 in case of errors.
     */
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

        try {
            bookingService.validateBookingCreation(currentUser, dto.userId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body("You are not authorized to create a booking for another user.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }

        Booking booking = bookingMapper.toEntity(dto);
        bookingService.save(booking);

        URI location = fromCurrentRequest().path("/{id}")
                .buildAndExpand(booking.getId()).toUri();

        return ResponseEntity.created(location).body(bookingMapper.toDto(booking));
    }

    /**
     * Deletes a booking by ID if the user is admin or owner.
     *
     * @param id ID of the booking.
     * @return 204 if deleted, 403/404 in case of error.
     */
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

    /**
     * Gets the authenticated user's active booking.
     *
     * @return Booking if found, 404 if not found.
     */
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

    /**
     * Performs advanced search on bookings by optional parameters.
     *
     * @param query Optional keyword (username, email, or phone).
     * @param shift Optional shift filter.
     * @param restaurantId Optional restaurant ID.
     * @param date Optional date filter.
     * @return List of bookings matching filters.
     */
    @Operation(summary = "Advanced search for bookings", description = "Search bookings by user info, shift, restaurantId and/or date")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search results returned")
    })
    @GetMapping("/search")
    public List<BookingDTO> advancedSearch(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String shift,
            @RequestParam(required = false) Long restaurantId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return bookingService.advancedSearch(query, shift, restaurantId, date).stream()
                .map(bookingMapper::toDto)
                .toList();
    }
}
