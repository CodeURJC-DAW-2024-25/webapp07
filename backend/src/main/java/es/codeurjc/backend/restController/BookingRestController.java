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
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody BookingDTO dto) {
        Booking booking = bookingMapper.toEntity(dto);
        bookingService.save(booking);
        URI location = fromCurrentRequest().path("/{id}")
                .buildAndExpand(booking.getId()).toUri();
        return ResponseEntity.created(location).body(bookingMapper.toDto(booking));
    }

    @Operation(summary = "Delete a booking")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
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
}
