package es.codeurjc.backend.controller;

import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String passwordConfirm,
                               @RequestParam String dateOfBirth,
                               Model model) {
        System.out.println("✅ registerUser method invoked");
        System.out.println("📩 Registration data -> Username: " + username + ", Email: " + email + ", Date of Birth: " + dateOfBirth);

        // Password validation
        if (!password.equals(passwordConfirm)) {
            System.out.println("⚠️ Error: Passwords do not match");
            model.addAttribute("error", "Passwords do not match.");
            return "login";
        }

//        if (password.length() < 8 || !password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*") || !password.matches(".*[@$!%*#?&].*")) {
//            System.out.println("⚠️ Error: Password does not meet requirements");
//            model.addAttribute("error", "Password must be at least 8 characters long and include a letter, a number, and a special character.");
//            return "login";
//        }

        if (userService.existsByUsername(username)) {
            System.out.println("⚠️ Error: Username '" + username + "' is already taken");
            model.addAttribute("error", "The username is already taken.");
            return "login";
        }

        if (userService.existsByEmail(email)) {
            System.out.println("⚠️ Error: Email '" + email + "' is already in use");
            model.addAttribute("error", "The email is already in use.");
            return "login";
        }

        System.out.println("✅ Data validated successfully, creating user...");

        LocalDate dob;
        try {
            dob = LocalDate.parse(dateOfBirth);
        } catch (DateTimeParseException e) {
            System.out.println("❌ Error: Invalid date format");
            model.addAttribute("error", "Invalid date format. Please use yyyy-MM-dd.");
            return "login";
        }

        User user = new User(username, password, "", "", dob, "", "", email, "USER");

        try {
            userService.registerUser(user);
            System.out.println("✅ User '" + username + "' registered successfully");
            model.addAttribute("message", "Registration successful!");
        } catch (Exception e) {
            System.out.println("❌ Error registering user: " + e.getMessage());
            model.addAttribute("error", "An error occurred during registration.");
        }

        return "redirect:/";
    }
}