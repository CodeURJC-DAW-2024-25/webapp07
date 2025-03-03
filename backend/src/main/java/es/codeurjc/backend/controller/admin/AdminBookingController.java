package es.codeurjc.backend.controller.admin;

import es.codeurjc.backend.model.Booking;
import es.codeurjc.backend.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller

public class AdminBookingController {

    @Autowired
    private BookingService bookingService;

    // Mostrar todas las reservas activas
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

    // Eliminar una reserva
    @PostMapping("/admin/bookings/{id}/delete")
    public String deleteBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookingService.cancelBookingById(id);
        redirectAttributes.addFlashAttribute("message", "Booking deleted successfully.");
        return "redirect:/admin/bookings";
    }
}


