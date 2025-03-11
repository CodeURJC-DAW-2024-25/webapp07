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

/**
 * Controller for managing user bookings.
 */
@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    /**
     * Displays the booking form.
     *
     * @param model The model to pass data to the view.
     * @param loggedUser The authenticated user.
     * @return The booking form page.
     */
    @GetMapping("/booking")
    public String showBookingForm(Model model, @AuthenticationPrincipal UserDetails loggedUser) {
        Optional<User> userOpt = userService.findByUsername(loggedUser.getUsername());
        if (userOpt.isEmpty()) {
            model.addAttribute("error", "User not found.");
            return "redirect:/";
        }

        User user = userOpt.get();
        Optional<Booking> activeBooking = bookingService.findActiveBookingByUser(user);
        if (activeBooking.isPresent()) {
            model.addAttribute("pageTitle", "Existing Reservation");
            model.addAttribute("message", "You already have an active reservation. To make a new one, please cancel your existing booking in your profile.");
            return "booking-existing";
        }

        List<Restaurant> restaurants = restaurantService.findAll();
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Booking");
        model.addAttribute("restaurants", restaurants);
        return "booking";
    }

    /**
     * Processes a new booking request.
     *
     * @param restaurantId The ID of the restaurant.
     * @param date The booking date.
     * @param shift The selected shift (Lunch or Dinner).
     * @param numPeople The number of people in the reservation.
     * @param loggedUser The authenticated user.
     * @param redirectAttributes Redirect attributes for displaying messages.
     * @return Redirects to the booking confirmation or back to the form if an error occurs.
     */
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
        Optional<Restaurant> restaurantOpt = restaurantService.findById(restaurantId);
        if (restaurantOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Selected restaurant not found.");
            return "redirect:/booking";
        }

        Restaurant restaurant = restaurantOpt.get();
        boolean success = bookingService.createBooking(restaurant, user, date, shift, numPeople);
        if (success) {
            return "booking-confirmation";
        } else {
            redirectAttributes.addFlashAttribute("error", "Could not make the reservation. Check availability.");
            return "redirect:/booking";
        }
    }

    /**
     * Displays the booking confirmation page.
     *
     * @param model The model to pass data to the view.
     * @return The booking confirmation page.
     */
    @GetMapping("/booking/confirmation")
    public String showConfirmationPage(Model model) {
        model.addAttribute("pageTitle", "Booking Confirmation");
        return "booking-confirmation";
    }

    /**
     * Displays the active booking of the user.
     *
     * @param model The model to pass data to the view.
     * @param loggedUser The authenticated user.
     * @return The profile booking page.
     */
    @GetMapping("/booking/my-booking")
    public String showUserBooking(Model model, @AuthenticationPrincipal UserDetails loggedUser) {
        Optional<User> userOpt = userService.findByUsername(loggedUser.getUsername());
        if (userOpt.isPresent()) {
            Optional<Booking> bookingOpt = bookingService.findActiveBookingByUser(userOpt.get());
            if (bookingOpt.isPresent()) {
                model.addAttribute("booking", bookingOpt.get());
                return "redirect:/profile";
            }
        }
        return "redirect:/booking";
    }


    /**
     * Cancels an active booking.
     *
     * @param loggedUser The authenticated user.
     * @param redirectAttributes Redirect attributes for displaying messages.
     * @return Redirects to the profile or booking cancellation page.
     */
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
            return "redirect:/booking-cancelled";
        } else {
            redirectAttributes.addFlashAttribute("error", "No active booking found.");
            return "redirect:/profile";
        }
    }

    /**
     * Displays the booking cancellation confirmation page.
     *
     * @param model The model to pass data to the view.
     * @return The booking cancellation confirmation page.
     */
    @GetMapping("/booking-cancelled")
    public String showBookingCancelledPage(Model model) {
        model.addAttribute("pageTitle", "Booking Cancelled");
        return "booking-cancelled";
    }

    /**
     * Retrieves available seats for a given restaurant, date, and shift.
     *
     * @param restaurantId The ID of the restaurant.
     * @param date The date of the reservation.
     * @param shift The shift (Lunch or Dinner).
     * @return The number of available seats.
     */
    // BookingController.java
    @GetMapping("/booking/availability")
    @ResponseBody
    public int getAvailableSeats(@RequestParam Long restaurantId,
                                 @RequestParam LocalDate date,
                                 @RequestParam String shift) {
        return bookingService.getAvailableSeats(restaurantId, date, shift);
    }
}
