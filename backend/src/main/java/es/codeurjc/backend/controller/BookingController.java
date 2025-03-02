package es.codeurjc.backend.controller;

import es.codeurjc.backend.model.Booking;
import es.codeurjc.backend.model.Restaurant;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.UserService;
import es.codeurjc.backend.service.BookingService;
import es.codeurjc.backend.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private UserService userService;

    @GetMapping("/booking")
    public String showBookingForm(Model model, @AuthenticationPrincipal UserDetails loggedUser) {
        Optional<User> userOpt = userService.findByUsername(loggedUser.getUsername());
        if (userOpt.isEmpty()) {
            model.addAttribute("error", "User not found.");
            return "redirect:/";
        }

        User user = userOpt.get(); // Obtener el usuario real desde la BD

        Optional<Booking> activeBooking = bookingService.findActiveBookingByUser(user);
        if (activeBooking.isPresent()) {
            model.addAttribute("pageTitle", "Existing Reservation");
            model.addAttribute("message", "You already have an active reservation. To make a new one, please cancel your existing booking in your profile.");
            return "booking-existing";
        }

        List<Restaurant> restaurants = restaurantService.findAll();
        model.addAttribute("pageTitle", "Booking");
        model.addAttribute("restaurants", restaurants);
        return "booking";
    }

    @PostMapping("/booking/new")
    public String processBooking(@RequestParam Long restaurantId,
                                 @RequestParam LocalDate date,
                                 @RequestParam String shift,
                                 @RequestParam int numPeople,
                                 @AuthenticationPrincipal UserDetails loggedUser,
                                 RedirectAttributes redirectAttributes) {

        Optional<User> userOpt = userService.findByUsername(loggedUser.getUsername());
        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/booking";
        }

        User user = userOpt.get();

        Optional<Booking> activeBooking = bookingService.findActiveBookingByUser(user);
        if (activeBooking.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "You already have an active reservation. Please cancel it first.");
            return "booking-existing";
        }

        Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
        if (restaurant.isPresent()) {
            boolean success = bookingService.createBooking(restaurant.get(), user, date, shift, numPeople);
            if (success) {
                return "booking-confirmation";
            } else {
                redirectAttributes.addFlashAttribute("error", "Could not make the reservation. Check availability.");
                return "redirect:/booking";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Selected restaurant not found.");
            return "redirect:/booking";
        }
    }

    @GetMapping("/booking/confirmation")
    public String showConfirmationPage(Model model) {
        model.addAttribute("pageTitle", "Booking Confirmation");
        return "booking-confirmation";
    }

    @GetMapping("/booking/my-booking")
    public String showUserBooking(Model model, @AuthenticationPrincipal UserDetails loggedUser) {
        Optional<User> userOpt = userService.findByUsername(loggedUser.getUsername());
        if (userOpt.isPresent()) {
            Optional<Booking> booking = bookingService.findActiveBookingByUser(userOpt.get());
            booking.ifPresent(value -> model.addAttribute("booking", value));
        }
        return "profile-booking";
    }

    @PostMapping("/booking/cancel")
    public String cancelBooking(@AuthenticationPrincipal UserDetails loggedUser, RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = userService.findByUsername(loggedUser.getUsername());
        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/profile";
        }

        User user = userOpt.get();
        Optional<Booking> booking = bookingService.findActiveBookingByUser(user);

        if (booking.isPresent()) {
            bookingService.cancelBooking(booking.get());
            redirectAttributes.addFlashAttribute("message", "Booking canceled successfully.");
            return "redirect:/booking-cancelled"; // ✅ Redirige a la página correcta
        } else {
            redirectAttributes.addFlashAttribute("error", "No active booking found.");
            return "redirect:/profile";
        }
    }

    @GetMapping("/booking-cancelled")
    public String showBookingCancelledPage(Model model) {
        model.addAttribute("pageTitle", "Booking Cancelled");
        return "booking-cancelled"; // Debe coincidir con el nombre del archivo HTML
    }


    @GetMapping("/booking/availability")
    @ResponseBody
    public int getAvailableSeats(@RequestParam Long restaurantId,
                                 @RequestParam LocalDate date,
                                 @RequestParam String shift) {
        List<Booking> existingBookings = bookingService.findBookingsByRestaurantAndShift(restaurantId, date, shift);
        int totalPeopleReserved = existingBookings.stream().mapToInt(Booking::getNumPeople).sum();
        return Math.max(40 - totalPeopleReserved, 0);
    }
}
