package es.codeurjc.backend.controller.admin;

import es.codeurjc.backend.model.Booking;
import es.codeurjc.backend.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for managing bookings in the admin panel.
 */
@Controller
public class AdminBookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Retrieves all active bookings and displays them in the admin panel.
     *
     * @param model The model to pass booking data to the view.
     * @return The template for managing bookings.
     */
    @GetMapping("/admin/bookings")
    public String showAllBookings(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        model.addAttribute("modalId", "confirmationModal");
        model.addAttribute("confirmButtonId", "confirmAction");
        model.addAttribute("modalMessage", "Are you sure you want to proceed with this action?");
        model.addAttribute("hasBookings", !bookings.isEmpty());
        return "admin/manage-bookings";
    }

    /**
     * Deletes a booking by its ID.
     *
     * @param id The ID of the booking to be deleted.
     * @param redirectAttributes Attributes to pass messages after redirection.
     * @return Redirects to the bookings management page after deletion.
     */
    @PostMapping("/admin/bookings/{id}/delete")
    public String deleteBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookingService.cancelBookingById(id);
        redirectAttributes.addFlashAttribute("message", "Booking deleted successfully.");
        return "redirect:/admin/bookings";
    }
}