package es.codeurjc.backend.controller;

import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

//    @PostMapping("/login")
//    public String loginUser(@RequestParam String username, @RequestParam String password, Model model) {
//        System.out.println("🔑 Intentando iniciar sesión con usuario: " + username);
//
//        Optional<User> user = userService.authenticate(username, password);
//        if (user.isPresent()) {
//            System.out.println("✅ Inicio de sesión exitoso para: " + username);
//            model.addAttribute("message", "Login successful!");
//            return "redirect:/";
//        } else {
//            System.out.println("❌ Error: Credenciales inválidas para: " + username);
//            model.addAttribute("error", "Invalid credentials");
//            return "redirect:/";
//        }
//    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String passwordConfirm,
                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirth,
                               Model model) {
        System.out.println("✅ Se ha invocado el método registerUser");
        System.out.println("📩 Datos recibidos en registro -> Name: " + name + ", Email: " + email + ", Username: " + username + ", Date of Birth: " + dateOfBirth);

        if (!password.equals(passwordConfirm)) {
            System.out.println("⚠️ Error: Las contraseñas no coinciden");
            model.addAttribute("error", "Passwords do not match");
            return "login";
        }

        if (userService.existsByUsername(username)) {
            System.out.println("⚠️ Error: El username '" + username + "' ya está registrado");
            model.addAttribute("error", "The username is already taken.");
            return "login";
        }

        if (userService.existsByEmail(email)) {
            System.out.println("⚠️ Error: El email '" + email + "' ya está registrado");
            model.addAttribute("error", "The email is already in use.");
            return "login";
        }

        System.out.println("✅ Datos validados correctamente, creando usuario...");

        User user = new User(username, password, name, "", dateOfBirth, "", "", email, "USER");

        try {
            userService.registerUser(user);
            System.out.println("✅ Usuario '" + username + "' registrado correctamente");
            model.addAttribute("message", "Registration successful!");
        } catch (Exception e) {
            System.out.println("❌ Error al registrar usuario: " + e.getMessage());
            model.addAttribute("error", "An error occurred during registration.");
        }

        return "redirect:/";
    }

}
