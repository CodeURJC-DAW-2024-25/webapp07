package es.codeurjc.backend.controller;

import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.UserService;
import es.codeurjc.backend.model.Booking;
import es.codeurjc.backend.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;

/**
 * Controller responsible for managing the user profile, including displaying and updating profile information.
 */
@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;


    /**
     * Displays the user profile page.
     *
     * @param userDetails The authenticated user details.
     * @param edit        A flag indicating whether the profile should be shown in edit mode.
     * @param model       The model to pass attributes to the view.
     * @return The profile view name.
     */
    @GetMapping("/profile")
    public String showProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false, defaultValue = "false") boolean edit,
            Model model) {

        String username = userDetails.getUsername();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Buscar la reserva activa del usuario
        Optional<Booking> activeBooking = bookingService.findActiveBookingByUser(user);
        activeBooking.ifPresent(booking -> model.addAttribute("booking", booking));

        model.addAttribute("user", user);
        model.addAttribute("editMode", edit);
        model.addAttribute("pageTitle", "Profile");

        // Attributes for confirmation modal
        model.addAttribute("modalId", "confirmationModal");
        model.addAttribute("confirmButtonId", "confirmSave");
        model.addAttribute("modalMessage", "Are you sure you want to save these changes?");

        return "profile";
    }


    /**
     * Updates the user profile with the provided information.
     *
     * @param userDetails The authenticated user details.
     * @param firstName   The updated first name.
     * @param lastName    The updated last name.
     * @param email       The updated email.
     * @param phoneNumber The updated phone number.
     * @param address     The updated address.
     * @param model       The model to pass attributes to the view.
     * @return A redirect to the profile page after updating.
     */
    @PostMapping("/profile")
    public String updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam String address,
            Model model) {

        String username = userDetails.getUsername();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update user details
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setAddress(address);

        userService.updateUser(user);

        return "redirect:/profile";
    }
}
