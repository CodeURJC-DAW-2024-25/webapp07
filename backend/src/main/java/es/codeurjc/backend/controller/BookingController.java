package es.codeurjc.backend.controller;

import es.codeurjc.backend.dto.RestaurantDTO;
import es.codeurjc.backend.dto.UserDTO;
import es.codeurjc.backend.mapper.RestaurantMapper;
import es.codeurjc.backend.model.Booking;
import es.codeurjc.backend.model.Restaurant;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.BookingService;
import es.codeurjc.backend.service.RestaurantService;
import es.codeurjc.backend.service.UserService;
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
 * Controller for managing restaurant bookings in the traditional web interface.
 */
@Controller
public class BookingController {

    private final BookingService bookingService;
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final RestaurantMapper restaurantMapper;

    /**
     * Constructor-based dependency injection.
     */
    public BookingController(BookingService bookingService,
                             RestaurantService restaurantService,
                             UserService userService,
                             RestaurantMapper restaurantMapper) {
        this.bookingService = bookingService;
        this.restaurantService = restaurantService;
        this.userService = userService;
        this.restaurantMapper = restaurantMapper;
    }

    /**
     * Displays the booking form.
     *
     * @param model the model to populate view data
     * @param loggedUser the authenticated user
     * @return the booking form view or redirect if user has an active booking
     */
    @GetMapping("/booking")
    public String showBookingForm(Model model, @AuthenticationPrincipal UserDetails loggedUser) {
        Optional<UserDTO> userOpt = userService.getAuthenticatedUserDto();
        if (userOpt.isEmpty()) {
            model.addAttribute("error", "User not found.");
            return "redirect:/";
        }

        UserDTO user = userOpt.get();

        if (bookingService.hasActiveBookingByUserId(user.id())) {
            model.addAttribute("pageTitle", "Existing Reservation");
            model.addAttribute("message", "You already have an active reservation. To make a new one, please cancel your existing booking in your profile.");
            return "booking-existing";
        }

        List<RestaurantDTO> restaurants = restaurantService.findAll();

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Booking");
        model.addAttribute("restaurants", restaurants);

        return "booking";
    }


    /**
     * Processes a new booking request.
     *
     * @param restaurantId the selected restaurant ID
     * @param date the booking date
     * @param shift the booking shift (LUNCH or DINNER)
     * @param numPeople number of people in the reservation
     * @param loggedUser the authenticated user
     * @param redirectAttributes attributes for flash messages
     * @return redirect to confirmation or back to booking on error
     */
    @PostMapping("/booking/new")
    public String processBooking(@RequestParam Long restaurantId,
                                 @RequestParam LocalDate date,
                                 @RequestParam String shift,
                                 @RequestParam int numPeople,
                                 @AuthenticationPrincipal UserDetails loggedUser,
                                 RedirectAttributes redirectAttributes) {

        Optional<UserDTO> userOpt = userService.getAuthenticatedUserDto();
        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/booking";
        }

        UserDTO userDTO = userOpt.get();

        try {
            bookingService.validateBookingCreation(userDTO.id());
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/booking";
        }

        Optional<RestaurantDTO> restaurantOpt = restaurantService.findById(restaurantId);
        if (restaurantOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Selected restaurant not found.");
            return "redirect:/booking";
        }

        RestaurantDTO restaurantDTO = restaurantOpt.get();

        boolean success = bookingService.createBooking(restaurantDTO, userDTO, date, shift, numPeople);
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
     * @param model the model to pass data to the view
     * @return confirmation view
     */
    @GetMapping("/booking/confirmation")
    public String showConfirmationPage(Model model) {
        model.addAttribute("pageTitle", "Booking Confirmation");
        return "booking-confirmation";
    }

    /**
     * Displays the user's active booking if available.
     *
     * @param model the model to pass data to the view
     * @param loggedUser the authenticated user
     * @return profile page if booking exists, otherwise redirect to booking page
     */
    @GetMapping("/booking/my-booking")
    public String showUserBooking(Model model, @AuthenticationPrincipal UserDetails loggedUser) {
        Optional<UserDTO> userOpt = userService.getAuthenticatedUserDto();
        if (userOpt.isPresent()) {
            Optional<Booking> bookingOpt = bookingService.findActiveBookingByUserId(userOpt.get().id());
            if (bookingOpt.isPresent()) {
                model.addAttribute("booking", bookingOpt.get());
                return "redirect:/profile";
            }
        }
        return "redirect:/booking";
    }


    /**
     * Cancels an active booking for the current user.
     *
     * @param loggedUser the authenticated user
     * @param redirectAttributes attributes for feedback messages
     * @return redirect to cancellation page or profile
     */
    @PostMapping("/booking/cancel")
    public String cancelBooking(@AuthenticationPrincipal UserDetails loggedUser, RedirectAttributes redirectAttributes) {
        Optional<UserDTO> userOpt = userService.getAuthenticatedUserDto();
        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/profile";
        }

        Long userId = userOpt.get().id();
        Optional<Booking> booking = bookingService.findActiveBookingByUserId(userId);

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
     * Shows confirmation page for cancelled booking.
     *
     * @param model the model to pass data to the view
     * @return cancellation view
     */
    @GetMapping("/booking-cancelled")
    public String showBookingCancelledPage(Model model) {
        model.addAttribute("pageTitle", "Booking Cancelled");
        return "booking-cancelled";
    }

    /**
     * Retrieves number of available seats for a given restaurant, date, and shift.
     *
     * @param restaurantId the restaurant ID
     * @param date the selected date
     * @param shift the selected shift
     * @return number of available seats
     */
    @GetMapping("/booking/availability")
    @ResponseBody
    public int getAvailableSeats(@RequestParam Long restaurantId,
                                 @RequestParam LocalDate date,
                                 @RequestParam String shift) {
        return bookingService.getAvailableSeats(restaurantId, date, shift);
    }
}