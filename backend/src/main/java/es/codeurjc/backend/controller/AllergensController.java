package es.codeurjc.backend.controller;
import es.codeurjc.backend.enums.Allergens;
import es.codeurjc.backend.service.DishService;

import es.codeurjc.backend.model.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class AllergensController {
    /*@Autowired
    private DishService dishService;

    @GetMapping("/menu/{id}")
    public String getAllergens(Model model, @PathVariable long id) {
        Optional<Dish> dish = dishService.findById(id);
        if (dish.isPresent()) {
            List<Allergens> allergens = dish.get().getAllergens();
            model.addAttribute("allergens", allergens);
            return "dish-information";
        } else {
            return "menu";
        }
    }
    */
}
