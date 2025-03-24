package es.codeurjc.backend.controller.admin;

import es.codeurjc.backend.dto.UserDTO;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing users in the admin panel.
 * Provides functionality to list, search, ban, and unban users.
 */
@Controller
@RequestMapping("/admin/users")
public class UsersController {

    @Autowired
    private UserService userService;

    /**
     * Displays the user management view with a list of users.
     * Supports optional query filtering by username or email.
     *
     * @param model The model to populate attributes for the view.
     * @param query (Optional) A string to filter users by username or email.
     * @return The name of the Thymeleaf template to render (admin/manage-users).
     */
    @GetMapping
    public String listUsers(Model model, @RequestParam(required = false) String query) {
        List<UserDTO> users = (query != null && !query.isEmpty())
                ? userService.searchUsers(query)
                : userService.getAllUsers();

        System.out.println("Users passed to view: " + users.size());

        model.addAttribute("users", users);
        model.addAttribute("hasUsers", !users.isEmpty());

        // Modal configuration for confirmation dialogs
        model.addAttribute("modalId", "confirmationModal");
        model.addAttribute("confirmButtonId", "confirmAction");
        model.addAttribute("modalMessage", "Are you sure you want to proceed with this action?");

        return "admin/manage-users";
    }


    /**
     * Bans a user, preventing them from accessing the system.
     *
     * @param id The ID of the user to be banned.
     * @return Redirects to the user management page.
     */
    @PostMapping("/ban/{id}")
    public String banUser(@PathVariable Long id) {
        userService.banUser(id);
        return "redirect:/admin/users";
    }

    /**
     * Unbans a previously banned user, restoring their access.
     *
     * @param id The ID of the user to be unbanned.
     * @return Redirects to the user management page.
     */
    @PostMapping("/unban/{id}")
    public String unbanUser(@PathVariable Long id) {
        userService.unbanUser(id);
        return "redirect:/admin/users";
    }
}
