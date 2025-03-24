package es.codeurjc.backend.controller;

import es.codeurjc.backend.dto.UserDTO;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Controller responsible for handling authentication-related operations such as login and user registration.
 */
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * Displays the login page and handles login errors.
     *
     * @param session The HTTP session to retrieve potential error messages.
     * @param model   The model to pass attributes to the view.
     * @param error   An optional parameter indicating a login error.
     * @return The login view name.
     */
    @GetMapping("/login")
    public String login(HttpSession session, Model model, @RequestParam(value = "error", required = false) String error) {

        System.out.println("🔹 [LoginController] Session content: " + session.getAttribute("errorMessage"));

        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");

            System.out.println("🔹 [LoginController] Retrieved message from session: " + errorMessage);
        }

        System.out.println("📌 [LoginController] Message in LoginController: " + errorMessage);
        return "login";
    }


    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String passwordConfirm,
                               @RequestParam String dateOfBirth,
                               Model model,
                               HttpSession session) {

        System.out.println("✅ registerUser method invoked");
        System.out.println("📩 Registration data -> Username: " + username + ", Email: " + email + ", Date of Birth: " + dateOfBirth);

        if (!password.equals(passwordConfirm)) {
            System.out.println("⚠️ Error: Passwords do not match");
            session.setAttribute("errorMessage", "Passwords do not match.");
            return "redirect:/auth/login";
        }

        try {
            UserDTO userDTO = new UserDTO(
                    null,
                    username,
                    "", // firstName
                    "", // lastName
                    LocalDate.parse(dateOfBirth),
                    "", // phoneNumber
                    "", // address
                    email,
                    List.of("USER"),
                    false,
                    password
            );

            userService.registerUser(userDTO);

            System.out.println("✅ User '" + username + "' registered successfully");
            session.setAttribute("message", "Registration successful!");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ Error: " + e.getMessage());
            session.setAttribute("errorMessage", e.getMessage());
            return "redirect:/auth/login";
        } catch (Exception e) {
            System.out.println("❌ Unexpected error registering user: " + e.getMessage());
            session.setAttribute("errorMessage", "An unexpected error occurred during registration.");
            return "redirect:/auth/login";
        }
    }


}
