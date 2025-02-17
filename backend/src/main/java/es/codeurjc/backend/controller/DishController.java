package es.codeurjc.backend.controller;

import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.service.DishService;
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
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping("/new-dish")
    public String showNewDishForm() {
        return "new-dish";
    }
}