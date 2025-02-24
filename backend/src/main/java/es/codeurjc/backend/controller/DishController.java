package es.codeurjc.backend.controller;

import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.enums.Allergens;

import es.codeurjc.backend.service.DishService;
import org.hibernate.engine.jdbc.BlobProxy;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DishController {
    @Autowired
    private DishService dishService;

    @RequestMapping("/menu")
    public class MenuController {


    }
    @PostMapping("/menu/{id}/remove-dish")
    public String removeDish(Model model, @PathVariable long id, RedirectAttributes redirectAttributes) {
        Optional<Dish> dish = dishService.findById(id);
        if (dish.isPresent()) {
            dishService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Plato eliminado con éxito");
        }
        return "redirect:/menu";
    }

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

        List<Dish> dishes = dishService.findAll();
        model.addAttribute("dish", dishes.subList(0, Math.min(10, dishes.size())));
        return "menu";
    }

    @GetMapping("/api/dishes")
    @ResponseBody
    public List<Dish> getDishes(@RequestParam("offset") int offset,
                                @RequestParam("limit") int limit) {
        List<Dish> allDishes = dishService.findAll();
        int total = allDishes.size();
        int fromIndex = Math.min(offset, total);
        int toIndex = Math.min(offset + limit, total);
        return allDishes.subList(fromIndex, toIndex);
    }

    @GetMapping("/menu/{id}")
    public String showDishInfo(Model model, @PathVariable long id){

        Optional<Dish> dish = dishService.findById(id);
        if (dish.isPresent()) {
            model.addAttribute("dish", dish.get());
            return "dish-information";
        } else {
            return "menu";
        }
    }

    /*@GetMapping("/menu/{id}/edit-dish")
    public String showEditDishForm(Model model, @PathVariable long id) {


        Optional<Dish> dish = dishService.findById(id);
        if (dish.isPresent()) {
            List<Allergens> allergens = dish.get().getAllergens();
            model.addAttribute("allergens", Allergens.values());

            String ingredientsFormatted = String.join(", ", dish.get().getIngredients());

            model.addAttribute("ingredients", ingredientsFormatted);
            model.addAttribute("dish", dish.get());

            return "dish-form";
        } else {
            return "menu";
        }
    }
    @GetMapping("/menu/new-dish")
    public String showNewDishForm(Model model) {
        model.addAttribute("allergens", Allergens.values());
        return "dish-form";
    }*/

    @GetMapping({"/menu/new-dish", "/menu/{id}/edit-dish"})
    public String showDishForm(@PathVariable(required = false) Long id, Model model) {
        Dish dish;
        String formAction;

        if (id != null) { // Modo edición
            Optional<Dish> dishOpt = dishService.findById(id);
            if (dishOpt.isPresent()) {
                dish = dishOpt.get();

                List<Allergens> allergens = dish.getAllergens();
                model.addAttribute("allergens",allergens);

                formAction = "/menu/" + dish.getId() + "/edit-dish";

                String ingredientsFormatted = String.join(", ", dish.getIngredients());
                model.addAttribute("ingredients", ingredientsFormatted);

                model.addAttribute("dish", dish);
            } else {
                // Si no se encuentra, redirige o maneja el error
                return "redirect:/menu";
            }
        } else { // Modo creación
            dish = new Dish();
            model.addAttribute("allergens", Allergens.values());
            formAction = "/menu/new-dish";
        }

        // Agregar al modelo
        model.addAttribute("formAction", formAction);

        return "dish-form";
    }


    @PostMapping({"/menu/new-dish", "/menu/{id}/edit-dish"})
    public String editDishProcess(Model model, Dish dish, String ingredients, String action, MultipartFile imageField, @RequestParam List<Allergens> selectedAllergens, @RequestParam boolean vegan) throws IOException {
        List<String> ingredientsList = Arrays.stream(ingredients.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        dish.setIngredients(ingredientsList);

        dish.setAllergens(selectedAllergens);
        dish.setVegan(vegan);

        if (!imageField.isEmpty()) {
            dish.setDishImagefile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
            dish.setImage(true);
        }

        model.addAttribute("dishId", dish.getId());

        if ("save".equals(action)){
            dishService.save(dish);
            return "redirect:/menu/" + dish.getId();
        }
        return "dish-form";
    }

    /*@PostMapping("/menu/new-dish")
    public String newDishProcess(Model model, Dish dish, String ingredients, String action, MultipartFile imageField, @RequestParam List<Allergens> selectedAllergens, @RequestParam boolean vegan) throws IOException {
        List<String> ingredientsList = Arrays.stream(ingredients.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        dish.setIngredients(ingredientsList);

        dish.setAllergens(selectedAllergens);
        dish.setVegan(vegan);

        if (!imageField.isEmpty()) {
            dish.setDishImagefile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
            dish.setImage(true);
        }

        model.addAttribute("dishId", dish.getId());

        if ("save".equals(action)){
            dishService.save(dish);
            return "redirect:/menu/" + dish.getId();
        }
        return "dish-form";
    }*/

    }
