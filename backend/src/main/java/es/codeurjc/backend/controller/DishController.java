package es.codeurjc.backend.controller;

import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class DishController {
    @Autowired
    private DishService dishService;

    @GetMapping("/menu/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {

        Optional<Dish> op = dishService.findById(id);

        if (op.isPresent() && op.get().getDishImagefile() != null) {

            Blob image = op.get().getDishImagefile();
            Resource file = new InputStreamResource(image.getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(image.length()).body(file);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/menu")
    public String showMenu(Model model){

        model.addAttribute("dish", dishService.findAll());
        return "menu";
    }

    @GetMapping("/menu/{id}")
    public String showDishInfo(Model model, @PathVariable long id){
        model.addAttribute("dish", dishService.findById(id));
        return "dish-information";
    }


    /*@PostMapping("/menu/new-dish")
    public String showNewDishForm(Model model,@RequestParam String dishName, @RequestParam String description, @RequestParam int price, @RequestParam String ingredients, @RequestParam boolean vegan, @RequestParam File image) {
        Dish newDish = new Dish(dishName, description, price, ingredients, vegan, image);
        return "admin-actions-confirm";
    }*/
}