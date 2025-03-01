package es.codeurjc.backend.controller;

import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.enums.Allergens;

import es.codeurjc.backend.service.DishService;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DishController {
    @Autowired
    private DishService dishService;

    @GetMapping({"/menu", "/menu/filter", "/menu/sort"})
    public String showMenu(@RequestParam(required = false) String name,
                           @RequestParam(required = false) String ingredient,
                           @RequestParam(required = false) Integer maxPrice,
                           @RequestParam(defaultValue = "default") String sortBy, Model model) throws SQLException {

        List<Dish> dishes = filterDishes(name, ingredient, maxPrice);

        if (dishes.size() < 10){
            for (Dish dish : dishes) {
                int rate =  (int) Math.ceil(dish.getRates().stream().mapToInt(Integer::intValue).average().orElse(0));
                dish.setRate(rate);

                dish.setDishImagePath(dish.blobToString(dish.getDishImagefile(), dish));
            }
        }else{
            for (int i = 0; i < 10; i++) {
                int rate =  (int) Math.ceil(dishes.get(i).getRates().stream().mapToInt(Integer::intValue).average().orElse(0));
                dishes.get(i).setRate(rate);

                dishes.get(i).setDishImagePath(dishes.get(i).blobToString(dishes.get(i).getDishImagefile(), dishes.get(i)));
            }
        }

        if ("price".equals(sortBy)) {
            dishes.sort(Comparator.comparing(Dish::getPrice));
            model.addAttribute("isPrice", true);
        } else if ("rate".equals(sortBy)) {
            dishes.sort(Comparator.comparing(Dish::getRate).reversed());
            model.addAttribute("isRate", true);
        } else {
            model.addAttribute("isDefault", true);
        }

        model.addAttribute("dish", dishes.subList(0, Math.min(10, dishes.size())));

        model.addAttribute("pageTitle", "Menu");

        return "menu";
    }

    private List<Dish> filterDishes(String name, String ingredient, Integer maxPrice) {

        List<Dish> dishesByName =  (name != null  && !name.isEmpty())
                ? dishService.findByName(name)
                : dishService.findAll();

        List<Dish> dishesByIngredient = (ingredient != null && !ingredient.isEmpty())
                ? dishService.findByIngredient(ingredient)
                : dishService.findAll();

        List<Dish> dishesByPrice = (maxPrice != null)
                ? dishService.findBymaxPrice(maxPrice)
                : dishService.findAll();

        // More than 1 filter
        if (name != null && !name.isEmpty()) {
            dishesByName.retainAll(dishesByIngredient);
            dishesByName.retainAll(dishesByPrice);
            return dishesByName;  // Final list
        } else {
            dishesByIngredient.retainAll(dishesByPrice);
            return dishesByIngredient;  // Final list
        }
    }

    @GetMapping("/api/menu")
    @ResponseBody
    public List<Dish> getDishes(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int pageSize) throws SQLException {
        Page<Dish> menuPage = dishService.findAllDishes(PageRequest.of(page, pageSize));
        for (Dish currentDish : menuPage.getContent()) {
            currentDish.setDishImagePath(currentDish.blobToString(currentDish.getDishImagefile(), currentDish));
        }

        return menuPage.getContent();
    }

    @GetMapping("/menu/{id}")
    public String showDishInfo(Model model, @PathVariable long id) throws SQLException {

        Optional<Dish> dish = dishService.findById(id);
        if (dish.isPresent()) {
            dish.get().setDishImagePath(dish.get().blobToString(dish.get().getDishImagefile(), dish.get()));

            int rate =  (int) Math.ceil(dish.get().getRates().stream().mapToInt(Integer::intValue).average().orElse(0));

            List<Integer> starList = new ArrayList<>();
            List<Integer> noStarList = new ArrayList<>();

            for (int i = 0; i < rate; i++){
                starList.add(0);
            }
            for (int i =rate; i < 5; i++){
                noStarList.add(0);
            }

            model.addAttribute("stars", starList);
            model.addAttribute("noStars", noStarList);


            List<String> formattedIngredients = dish.get().getIngredients().stream()
                    .map(ing -> ing.substring(0, 1).toUpperCase() + ing.substring(1).toLowerCase())
                    .toList();

            model.addAttribute("ingredients", formattedIngredients);

            model.addAttribute("dish", dish.get());

            model.addAttribute("pageTitle", "Dish info");

            model.addAttribute("modalId", "confirmationModal");
            model.addAttribute("confirmButtonId", "confirmAction");
            model.addAttribute("modalMessage", "Are you sure you want to proceed with this action?");

            return "dish-information";
        } else {
            return "menu";
        }
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

    @PostMapping("/menu/{id}/admin/remove-dish")
    public String removeDish(Model model, @PathVariable long id, RedirectAttributes redirectAttributes) {
        Optional<Dish> dish = dishService.findById(id);
        if (dish.isPresent()) {
            dishService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Plato eliminado con éxito");
        }
        return "redirect:/menu";
    }

    @PostMapping("/menu/{id}/admin/mark-unavailable-dish")
    public String markUnavailableDish(Model model,Dish dish, @PathVariable long id, RedirectAttributes redirectAttributes) {

        dish.setAvailable(false);
        dishService.save(dish);
        redirectAttributes.addFlashAttribute("message", "Plato deshabilitado con éxito");

        return "redirect:/menu";
    }

    @GetMapping({"/menu/admin/new-dish", "/menu/{id}/admin/edit-dish"})
    public String showDishForm(@PathVariable(required = false) Long id, Model model, HttpServletRequest request) throws SQLException {
        Dish dish;
        String formAction;

        if (id != null) { // Modo edición
            Optional<Dish> dishOpt = dishService.findById(id);
            if (dishOpt.isPresent()) {

                dish = dishOpt.get();
                dish.setDishImagePath(dish.blobToString(dish.getDishImagefile(), dish));

                formAction = "/menu/" + dish.getId() + "/admin/edit-dish";

                model.addAttribute("imageFile", dish.getDishImagefile());

                String ingredientsFormatted = String.join(", ", dish.getIngredients());
                model.addAttribute("ingredients", ingredientsFormatted);

                model.addAttribute("allergens", Allergens.values());

                model.addAttribute("dish", dish);
            } else {
                // Si no se encuentra, redirige o maneja el error
                return "redirect:/menu";
            }
        } else { // Modo creación
            dish = new Dish();
            model.addAttribute("allergens", Allergens.values());
            model.addAttribute("pageTitle", "New dish");
            formAction = "/menu/admin/new-dish";
        }

        model.addAttribute("modalId", "confirmationModal");
        model.addAttribute("confirmButtonId", "confirmAction");
        model.addAttribute("modalMessage", "Are you sure you want to proceed with this action?");
        // Agregar al modelo
        model.addAttribute("formAction", formAction);
        model.addAttribute("pageTitle", "Edit dish");
        return "dish-form";
    }


    @PostMapping({"/menu/admin/new-dish/save", "/menu/{id}/admin/edit-dish/save"})
    public String editDishProcess(Model model, Dish dish, String ingredients, String action, @RequestParam MultipartFile imageField, @RequestParam List<Allergens> selectedAllergens, boolean vegan) throws IOException {
        List<String> ingredientsList = Arrays.stream(ingredients.toLowerCase().split(","))
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

        if ("save".equals(action)) {
            dishService.save(dish);
            return "redirect:/menu/" + dish.getId();
        }
        return "dish-form";
    }
}
