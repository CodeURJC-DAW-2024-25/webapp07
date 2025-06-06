package es.codeurjc.backend.controller.admin;

import es.codeurjc.backend.dto.DishDTO;
import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing dishes in the admin panel.
 * Provides functionality to list, search, and delete dishes.
 */
@Controller
@RequestMapping("/admin/dishes")
public class DishesController {

    @Autowired
    private DishService dishService;

    /**
     * Displays the dish management page with a list of dishes.
     * Allows searching for dishes by name or description.
     *
     * @param model The model to pass attributes to the view.
     * @param query Optional search query to filter dishes.
     * @return The admin dish management view name.
     */
    @GetMapping
    public String listDishes(Model model, @RequestParam(required = false) String query) {
        List<DishDTO> dishes;

        if (query != null && !query.isEmpty()) {
            dishes = dishService.searchDishes(query);
        } else {
            dishes = dishService.findAll();
        }

        System.out.println("Dishes passed to view: " + dishes.size());

        model.addAttribute("dishes", dishes);
        model.addAttribute("hasDishes", !dishes.isEmpty());

        // Modal configuration for confirmation dialogs
        model.addAttribute("modalId", "confirmationModal");
        model.addAttribute("confirmButtonId", "confirmAction");
        model.addAttribute("modalMessage", "Are you sure you want to proceed with this action?");

        return "admin/manage-dishes";
    }
    /**
     * Marks a dish as unavailable.
     *
     * @param id The ID of the dish to be marked as unavailable.
     * @return Redirects to the dish management page.
     */
    @PostMapping("/mark-unavailable-dish/{id}")
    public String markUnavailable(@PathVariable Long id) {
        dishService.disableById(id);
        return "redirect:/admin/dishes";
    }
    /**
     * Marks a dish as available.
     *
     * @param id The ID of the dish to be marked as available.
     * @return Redirects to the dish management page.
     */
    @PostMapping("/mark-available-dish/{id}")
    public String markAvailable(@PathVariable Long id) {
        dishService.enableById(id);
        return "redirect:/admin/dishes";
    }
}
