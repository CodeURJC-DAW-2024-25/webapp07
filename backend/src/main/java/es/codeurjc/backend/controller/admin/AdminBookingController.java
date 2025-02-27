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
@RequestMapping("/admin/bookings")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBookingController {

    @Autowired
    private BookingService bookingService;

    // Mostrar todas las reservas activas
    @GetMapping
    public String showAllBookings(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();

        // Definir hasBookings como true si la lista no está vacía
        model.addAttribute("bookings", bookings);
        model.addAttribute("hasBookings", !bookings.isEmpty());

        return "manage-bookings";
    }

    // Eliminar una reserva
    @PostMapping("/{id}/delete")
    public String deleteBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookingService.cancelBookingById(id);
        redirectAttributes.addFlashAttribute("message", "Booking deleted successfully.");
        return "redirect:/admin/bookings";
    }
}

