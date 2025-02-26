package es.codeurjc.backend.controller.admin;

import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/dishes")
public class DishesController {

    @Autowired
    private DishService dishService;

    @GetMapping
    public String listDishes(Model model, @RequestParam(required = false) String query) {
        List<Dish> dishes;

        if (query != null && !query.isEmpty()) {
            dishes = dishService.searchDishes(query);
        } else {
            dishes = dishService.findAll();
        }

        System.out.println("Platos pasados a la vista: " + dishes.size());

        model.addAttribute("dishes", dishes);
        model.addAttribute("hasDishes", !dishes.isEmpty());

        model.addAttribute("modalId", "confirmationModal");
        model.addAttribute("confirmButtonId", "confirmAction");
        model.addAttribute("modalMessage", "Are you sure you want to proceed with this action?");

        return "admin/manage-dishes";
    }

    @PostMapping("/delete/{id}")
    public String deleteDish(@PathVariable Long id) {
        dishService.deleteById(id);
        return "redirect:/admin/dishes";
    }
}

