package es.codeurjc.backend.controller;

import es.codeurjc.backend.dto.UserDTO;
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


    @GetMapping("/profile")
    public String showProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false, defaultValue = "false") boolean edit,
            Model model) {

        String username = userDetails.getUsername();

        userService.findUserDtoByUsername(username).ifPresent(dto -> {
            bookingService.findActiveBookingByUserId(dto.id())
                    .ifPresent(booking -> model.addAttribute("booking", booking));

            model.addAttribute("user", dto);
        });

        model.addAttribute("editMode", edit);
        model.addAttribute("pageTitle", "Profile");

        // Attributes for confirmation modal
        model.addAttribute("modalId", "confirmationModal");
        model.addAttribute("confirmButtonId", "confirmSave");
        model.addAttribute("modalMessage", "Are you sure you want to save these changes?");

        return "profile";
    }





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

        UserDTO userDTO = userService.findUserDtoByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDTO updatedDTO = new UserDTO(
                userDTO.id(),
                userDTO.username(),
                firstName,
                lastName,
                userDTO.dateOfBirth(),
                phoneNumber,
                address,
                email,
                userDTO.roles(),
                userDTO.banned(),
                userDTO.password()
        );

        userService.updateUser(userDTO.id(), updatedDTO);

        return "redirect:/profile";
    }


}
