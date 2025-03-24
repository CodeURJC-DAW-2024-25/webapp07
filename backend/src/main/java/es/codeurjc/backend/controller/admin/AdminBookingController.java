package es.codeurjc.backend.controller.admin;

import es.codeurjc.backend.model.Booking;
import es.codeurjc.backend.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for managing bookings in the admin panel.
 */
@Controller
@RequestMapping("/admin/bookings")
public class AdminBookingController {

    private final BookingService bookingService;

    /**
     * Constructor-based dependency injection for BookingService.
     *
     * @param bookingService Service for managing booking operations.
     */
    public AdminBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Displays all active bookings in the admin panel, optionally filtered by a search query.
     *
     * @param model The model to pass booking data to the view.
     * @param query Optional search query (username, email or phone).
     * @return The template for managing bookings.
     */
    @GetMapping
    public String showAllBookings(Model model, @RequestParam(required = false) String query) {
        List<Booking> bookings = (query != null && !query.isBlank())
                ? bookingService.searchBookings(query)
                : bookingService.getAllBookings();

        model.addAttribute("bookings", bookings);
        model.addAttribute("hasBookings", !bookings.isEmpty());

        // Modal configuration for confirmation dialog
        model.addAttribute("modalId", "confirmationModal");
        model.addAttribute("confirmButtonId", "confirmAction");
        model.addAttribute("modalMessage", "Are you sure you want to proceed with this action?");

        return "admin/manage-bookings";
    }

    /**
     * Deletes a booking by its ID.
     *
     * @param id The ID of the booking to be deleted.
     * @param redirectAttributes Attributes to pass feedback to the view.
     * @return Redirect to the admin bookings list.
     */
    @PostMapping("/{id}/delete")
    public String deleteBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookingService.cancelBookingById(id);
        redirectAttributes.addFlashAttribute("message", "Booking deleted successfully.");
        return "redirect:/admin/bookings";
    }
}
