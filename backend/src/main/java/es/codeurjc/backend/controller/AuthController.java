package es.codeurjc.backend.controller;

import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;


    @GetMapping("/auth/login")
    public String showLoginForm() {
        return "login";
    }


    /*
    @PostMapping("/auth/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, Model model) {
        Optional<User> user = userService.authenticate(username, password);
        if (user.isPresent()) {
            model.addAttribute("message", "Login successful!");
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }


    @PostMapping("/auth/register")
    public String registerUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String passwordConfirm,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            Model model) {

        if (!password.equals(passwordConfirm)) {
            model.addAttribute("error", "Passwords do not match");
            return "login";
        }

        User user = new User();
        user.setUsername(username);
        user.setEncodedPassword(password);
        user.setEmail(email);
        user.setFirstName(name);
        user.setDateOfBirth(dateOfBirth);

        userService.registerUser(user);
        model.addAttribute("message", "Registration successful!");
        return "login";
    }*/
}