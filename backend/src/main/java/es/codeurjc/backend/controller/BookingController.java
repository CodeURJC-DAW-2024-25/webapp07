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
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private RestaurantService restaurantService;


    // Mostrar formulario de reserva
    @GetMapping("/booking")
    public String showBookingForm(Model model, @AuthenticationPrincipal User user) {
        // Verificar si el usuario tiene una reserva activa
        Optional<Booking> activeBooking = bookingService.findActiveBookingByUser(user);

        if (activeBooking.isPresent()) {
            // Redirigir a una nueva pantalla indicando que ya tiene una reserva activa
            model.addAttribute("pageTitle", "Existing Reservation");
            model.addAttribute("message", "You already have an active reservation. To make a new one, please cancel your existing booking in your profile.");
            return "booking-existing"; // Nueva plantilla que informará al usuario
        }

        List<Restaurant> restaurants = restaurantService.findAll();
        model.addAttribute("pageTitle", "Booking");
        model.addAttribute("restaurants", restaurants);
        return "booking";
    }


    // Procesar la reserva
    @PostMapping("/booking/new")
    public String processBooking(@RequestParam Long restaurantId,
                                 @RequestParam LocalDate date,
                                 @RequestParam String shift,
                                 @RequestParam int numPeople,
                                 @AuthenticationPrincipal User user,
                                 RedirectAttributes redirectAttributes) {

        // Verificar si el usuario ya tiene una reserva activa
        Optional<Booking> activeBooking = bookingService.findActiveBookingByUser(user);
        if (activeBooking.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "You already have an active reservation. Please cancel it first.");
            return "redirect:/booking/confirmation-existing"; // Redirigir a una pantalla de error
        }

        Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
        if (restaurant.isPresent()) {
            boolean success = bookingService.createBooking(restaurant.get(), user, date, shift, numPeople);
            if (success) {
                redirectAttributes.addFlashAttribute("message", "Booking confirmed successfully.");
                return "redirect:/booking/confirmation"; // Redirigir a la pantalla de confirmación
            } else {
                redirectAttributes.addFlashAttribute("error", "Could not make the reservation. Check availability.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Selected restaurant not found.");
        }
        return "redirect:/booking";
    }



    @GetMapping("/booking/confirmation")
    public String showConfirmationPage(Model model) {
        model.addAttribute("pageTitle", "Booking Confirmation");
        return "booking-confirmation";
    }


    // Mostrar la reserva activa del usuario
    @GetMapping("/booking/my-booking")
    public String showUserBooking(Model model, @AuthenticationPrincipal User user) {
        Optional<Booking> booking = bookingService.findActiveBookingByUser(user);
        booking.ifPresent(value -> model.addAttribute("booking", value));
        return "profile-booking";
    }

    // Cancelar una reserva
    @PostMapping("/booking/cancel")
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
    @GetMapping("/booking/availability")
    @ResponseBody
    public int getAvailableSeats(@RequestParam Long restaurantId,
                                 @RequestParam LocalDate date,
                                 @RequestParam String shift) {
        // Get all bookings for the restaurant on the selected date and shift
        List<Booking> existingBookings = bookingService.findBookingsByRestaurantAndShift(restaurantId, date, shift);

        // Sum the total number of reserved seats
        int totalPeopleReserved = existingBookings.stream().mapToInt(Booking::getNumPeople).sum();

        // Calculate available seats (max 40 per shift)
        return Math.max(40 - totalPeopleReserved, 0);
    }


}
