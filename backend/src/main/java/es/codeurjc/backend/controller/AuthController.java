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
     * <p>
     * The method performs the following checks:
     * <ul>
     *   <li>Ensures that the provided passwords match.</li>
     *   <li>Delegates user existence and email existence validation to {@code UserService}.</li>
     *   <li>Delegates user creation and password encryption to {@code UserService}.</li>
     * </ul>
     * If validation fails, the user is redirected to the login page with an error message.
     * If registration is successful, the user is redirected to the home page.
     * </p>
     *
     * @param username        The desired username for the new user.
     * @param email           The email address associated with the new user.
     * @param password        The password chosen by the user.
     * @param passwordConfirm The confirmation of the chosen password.
     * @param dateOfBirth     The user's date of birth in {@code yyyy-MM-dd} format.
     * @param model           The model used to pass attributes to the view.
     * @param session         The HTTP session used to store messages for redirection.
     * @return A redirect to the home page on success or the login page on failure.
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

        if (!password.equals(passwordConfirm)) {
            System.out.println("⚠️ Error: Passwords do not match");
            session.setAttribute("errorMessage", "Passwords do not match.");
            return "redirect:/auth/login";
        }

        try {
            userService.registerUser(username, email, password, dateOfBirth);

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
