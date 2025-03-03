package es.codeurjc.backend.controller;

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

    /**
     * Handles user registration by validating input data, checking for duplicate users,
     * and creating a new user account if valid.
     *
     * @param username        The username entered by the user.
     * @param email           The email address entered by the user.
     * @param password        The password entered by the user.
     * @param passwordConfirm The password confirmation entered by the user.
     * @param dateOfBirth     The date of birth entered by the user.
     * @param model           The model to pass attributes to the view.
     * @param session         The HTTP session to store messages for redirection.
     * @return A redirect to the login page or home page depending on success or failure.
     */
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

        // Password validation
        if (!password.equals(passwordConfirm)) {
            System.out.println("⚠️ Error: Passwords do not match");
            session.setAttribute("errorMessage", "Passwords do not match.");
            return "redirect:/login";
        }

        if (userService.existsByUsername(username)) {
            System.out.println("⚠️ Error: Username '" + username + "' is already taken");
            session.setAttribute("errorMessage", "The username is already taken.");
            return "redirect:/login";
        }

        if (userService.existsByEmail(email)) {
            System.out.println("⚠️ Error: Email '" + email + "' is already in use");
            session.setAttribute("errorMessage", "The email is already in use.");
            return "redirect:/login";
        }

        System.out.println("✅ Data validated successfully, creating user...");

        // Parse date of birth
        LocalDate dob;
        try {
            dob = LocalDate.parse(dateOfBirth);
        } catch (DateTimeParseException e) {
            System.out.println("❌ Error: Invalid date format");
            session.setAttribute("errorMessage", "Invalid date format. Please use yyyy-MM-dd.");
            return "redirect:/login";
        }

        // Create user instance
        User user = new User(username, password, "", "", dob, "", "", email, false, "USER");

        try {
            userService.registerUser(user);
            System.out.println("✅ User '" + username + "' registered successfully");
            session.setAttribute("message", "Registration successful!");
            return "redirect:/";
        } catch (Exception e) {
            System.out.println("❌ Error registering user: " + e.getMessage());
            session.setAttribute("errorMessage", "An error occurred during registration.");
            return "redirect:/login";
        }
    }
}
