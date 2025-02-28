package es.codeurjc.backend.controller;

import es.codeurjc.backend.model.Booking;
import es.codeurjc.backend.model.Restaurant;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.BookingService;
import es.codeurjc.backend.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private RestaurantService restaurantService;

    // Mostrar formulario de reserva
    @GetMapping("/new")
    public String showBookingForm(Model model) {
        List<Restaurant> restaurants = restaurantService.findAll();
        model.addAttribute("restaurants", restaurants);
        return "booking-form";
    }

    // Procesar la reserva
    @PostMapping("/new")
    public String processBooking(@RequestParam Long restaurantId,
                                 @RequestParam LocalDate date,
                                 @RequestParam String shift,
                                 @RequestParam int numPeople,
                                 @AuthenticationPrincipal User user,
                                 RedirectAttributes redirectAttributes) {
        Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
        if (restaurant.isPresent()) {
            boolean success = bookingService.createBooking(restaurant.get(), user, date, shift, numPeople);
            if (success) {
                redirectAttributes.addFlashAttribute("message", "Booking confirmed successfully.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Could not make the reservation. Check availability.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Selected restaurant not found.");
        }
        return "redirect:/booking/new";
    }

    // Mostrar la reserva activa del usuario
    @GetMapping("/my-booking")
    public String showUserBooking(Model model, @AuthenticationPrincipal User user) {
        Optional<Booking> booking = bookingService.findActiveBookingByUser(user);
        booking.ifPresent(value -> model.addAttribute("booking", value));
        return "profile-booking";
    }

    // Cancelar una reserva
    @PostMapping("/cancel")
    public String cancelBooking(@AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
        Optional<Booking> booking = bookingService.findActiveBookingByUser(user);
        if (booking.isPresent()) {
            bookingService.cancelBooking(booking.get());
            redirectAttributes.addFlashAttribute("message", "Booking canceled successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No active booking found.");
        }
        return "redirect:/profile";
    }
    @GetMapping("/availability")
    @ResponseBody
    public int getAvailableSeats(@RequestParam Long restaurantId,
                                 @RequestParam LocalDate date,
                                 @RequestParam String shift) {
        // Obtener todas las reservas del restaurante en esa fecha y turno
        List<Booking> existingBookings = bookingService.findBookingsByRestaurantAndShift(restaurantId, date, shift);

        // Sumar el total de personas ya reservadas
        int totalPeopleReserved = existingBookings.stream().mapToInt(Booking::getNumPeople).sum();

        // Calcular asientos disponibles (máximo 40 por turno)
        return Math.max(40 - totalPeopleReserved, 0);
    }

}
