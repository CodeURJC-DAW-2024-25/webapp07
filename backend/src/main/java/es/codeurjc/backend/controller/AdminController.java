package es.codeurjc.backend.controller;

import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(Model model, @RequestParam(required = false) String query) {
        List<User> users;

        if (query != null && !query.isEmpty()) {

            users = userService.searchUsers(query);
        } else {

            users = userService.getAllUsers();
        }

        System.out.println("Usuarios pasados a la vista: " + users.size());

        model.addAttribute("users", users);
        model.addAttribute("hasUsers", !users.isEmpty());

        return "admin/manage-users";
    }

    @PostMapping("/ban/{id}")
    public String banUser(@PathVariable Long id) {
        userService.banUser(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/unban/{id}")
    public String unbanUser(@PathVariable Long id) {
        userService.unbanUser(id);
        return "redirect:/admin/users";
    }
}
