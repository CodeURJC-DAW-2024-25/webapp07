package es.codeurjc.backend.controller;

import es.codeurjc.backend.dto.DishDTO;
import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.enums.Allergens;

import es.codeurjc.backend.service.DishService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

/**
 * Controller for handling dish-related operations.
 */
@Controller
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * Displays the menu with optional filtering.
     *
     * @param name       Filter by dish name.
     * @param ingredient Filter by ingredient.
     * @param maxPrice   Filter by maximum price.
     * @param model      Model to store attributes.
     * @return The menu page.
     * @throws SQLException If an SQL error occurs.
     */
    @GetMapping({"/menu", "/menu/filter", "/menu/sort"})
    public String showMenu(@RequestParam(required = false) String name,
                           @RequestParam(required = false) String ingredient,
                           @RequestParam(required = false) Integer maxPrice,
                           Model model) throws SQLException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());

        model.addAttribute("isAuthenticated", isAuthenticated);

        List<DishDTO> dishes = dishService.filterDishes(name, ingredient, maxPrice);

        model.addAttribute("dish", dishes.subList(0, Math.min(10, dishes.size())));
        model.addAttribute("pageTitle", "Menu");
        model.addAttribute("menuActive", true);
        return "menu";
    }

    /**
     * Retrieves a paginated list of dishes in JSON format.
     *
     * @param name       Filter by dish name.
     * @param ingredient Filter by ingredient.
     * @param maxPrice   Filter by maximum price.
     * @param page       Page number.
     * @param pageSize   Number of items per page.
     * @throws SQLException If an SQL error occurs.
     */
    @GetMapping("/api/menu")
    @ResponseBody
    public Map<String, Object> getDishes(@RequestParam(required = false) String name,
                                         @RequestParam(required = false) String ingredient,
                                         @RequestParam(required = false) Integer maxPrice,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int pageSize) throws SQLException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());

        List<DishDTO> filteredDishes = dishService.filterDishes(name, ingredient, maxPrice);
        filteredDishes = dishService.calculateRates(filteredDishes);

        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, filteredDishes.size());

        List<DishDTO> paginatedDishes = (fromIndex >= filteredDishes.size()) ?
                List.of() :
                filteredDishes.subList(fromIndex, toIndex);

        for (DishDTO dish : paginatedDishes) {
            if (dish.image()) {
                dish = dishService.setImagePath(dish);
            }
        }

        // Crear respuesta en un mapa JSON
        Map<String, Object> response = new HashMap<>();
        response.put("isAuthenticated", isAuthenticated);
        response.put("dishes", paginatedDishes);

        return response;
    }
    /**
     * Handles GET requests for displaying detailed information about a specific dish.
     * The dish is identified by its unique ID provided in the path.
     *
     * @param model The Spring MVC model used to pass data to the view.
     * @param id The unique identifier of the dish to display. This is extracted from the URL path.
     * @return The name of the view to render. Returns "dish-information" if the dish is found,
     * and "menu" if the dish with the given ID does not exist.
     * @throws SQLException If an SQL exception occurs during database interaction,
     * for example, while retrieving the dish or its associated data.
     */
    @GetMapping("/menu/{id}")
    public String showDishInfo(Model model, @PathVariable long id) throws SQLException {

        Optional<DishDTO> dish = dishService.findById(id);
        if (dish.isPresent()) {
            dish = Optional.ofNullable(dishService.setImagePath(dish.orElseThrow()));
            if (dish.get().image()) {

            }
            int rate = (int) Math.ceil(dish.get().rates().stream().mapToInt(Integer::intValue).average().orElse(0));

            model.addAttribute("stars", dishService.getStarList(rate));
            model.addAttribute("noStars", dishService.getNoStarList(rate));
            model.addAttribute("ingredients", dishService.formatIngredients(dish));
            model.addAttribute("dish", dish.get());
            model.addAttribute("pageTitle", "Dish info");
            model.addAttribute("menuActive", true);
            model.addAttribute("modalId", "confirmationModal");
            model.addAttribute("confirmButtonId", "confirmAction");
            model.addAttribute("modalMessage", "Are you sure you want to proceed with this action?");
            return "dish-information";
        } else {
            return "menu";
        }
    }
    /**
     * Retrieves an image of a dish.
     * @param id Dish ID.
     * @return The image file as a response entity.
     * @throws SQLException If an SQL error occurs.
     */
    @GetMapping("/menu/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {
        Optional<DishDTO> op = dishService.findById(id);
        if (op.isPresent() && op.get().dishImagefile() != null) {
            Blob image = op.get().dishImagefile();
            Resource file = new InputStreamResource(image.getBinaryStream());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(image.length()).body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Marks a dish as unavailable.
     * @param id                 Dish ID.
     * @param redirectAttributes Redirect attributes for messages.
     * @return Redirects to the menu page.
     */
    @PostMapping("/menu/{id}/admin/mark-unavailable-dish")
    public String markUnavailableDish(@PathVariable long id, RedirectAttributes redirectAttributes) {
        dishService.disableById(id);
        redirectAttributes.addFlashAttribute("message", "Plato deshabilitado con éxito");
        return "redirect:/menu";
    }

    /**
     * Displays the form for adding or editing a dish.
     * @param id    Dish ID (optional, required for editing).
     * @param model Model to store attributes.
     * @return The dish form page.
     * @throws SQLException If an SQL error occurs.
     */
    @GetMapping({"/menu/admin/new-dish", "/menu/{id}/admin/edit-dish"})
    public String showDishForm(@PathVariable(required = false) Long id, Model model) throws SQLException {
        String formAction;
        if (id != null) { // Modo edición
            model.addAttribute("pageTitle", "Edit dish");
            model.addAttribute("menuActive", true);
            Optional<DishDTO> dishOpt = dishService.findById(id);
            if (dishOpt.isPresent()) {
                DishDTO dish;
                dish = dishOpt.get();
                dish = dishService.setImagePath(dish);

                formAction = "/menu/" + dish.id() + "/admin/edit-dish";

                model.addAttribute("imageFile", dish.dishImagefile());

                String ingredientsFormatted = String.join(", ", dish.ingredients());
                model.addAttribute("ingredients", ingredientsFormatted);

                model.addAttribute("allergens", Allergens.values());

                model.addAttribute("dish", dish);
            } else {
                // Si no se encuentra, redirige o maneja el error
                return "redirect:/menu";
            }
        } else { // Modo creación
            model.addAttribute("allergens", Allergens.values());
            model.addAttribute("pageTitle", "New dish");
            model.addAttribute("menuActive", true);
            formAction = "/menu/admin/new-dish";
        }

        model.addAttribute("modalId", "confirmationModal");
        model.addAttribute("confirmButtonId", "confirmAction");
        model.addAttribute("modalMessage", "Are you sure you want to proceed with this action?");
        // Agregar al modelo
        model.addAttribute("formAction", formAction);

        model.addAttribute("pageTitle", "Dish form");

        return "dish-form";
    }

    /**
     * Processes the creation or edition of a dish.
     *
     * @param model             Model to store attributes.
     * @param dish              The dish to be saved.
     * @param ingredients       Ingredients list as a string.
     * @param action            The action to perform.
     * @param imageField        Dish image (optional).
     * @param selectedAllergens List of selected allergens (optional).
     * @param vegan             Indicates if the dish is vegan.
     * @return Redirects to the menu or dish form.
     * @throws IOException If an image processing error occurs.
     */
    @PostMapping({"/menu/admin/new-dish/save"})
    public String addDishProcess(Model model, Optional<Dish> dish, String ingredients, String action, @RequestParam(required = false) MultipartFile imageField, @RequestParam(required = false) List<Allergens> selectedAllergens, boolean vegan) throws IOException {

        Optional<Dish> finalDish = dishService.processNewDishInfo(dish, ingredients, selectedAllergens, vegan, imageField);

        if (finalDish.isPresent()) {
            model.addAttribute("dishId", finalDish.get().getId());

            String formAction = (finalDish.get().getId() != null) ? "/menu/" + finalDish.get().getId() + "/admin/edit-dish" : "/menu/admin/new-dish";
            model.addAttribute("formAction", formAction);

            if ("save".equals(action)) {
                dishService.save(finalDish.orElse(null));
                return "redirect:/menu/" + finalDish.get().getId();
            }
        }
        model.addAttribute("modalId", "confirmationModal");
        model.addAttribute("confirmButtonId", "confirmAction");
        model.addAttribute("modalMessage", "Are you sure you want to proceed with this action?");
        model.addAttribute("pageTitle", "Saved Changes");
        return "dish-form";
    }
    /**
     * Handles POST requests for saving changes made to an existing dish or creating a new dish.
     *
     * @param model The Spring MVC model used to pass data to the view.
     * @param dish An Optional containing the Dish object populated with form data.
     * @param ingredients A String containing the ingredients of the dish, typically as a comma-separated list.
     * @param action A String indicating the action to perform ("save" in this case).
     * @param imageField An optional MultipartFile representing the uploaded image for the dish.
     * @param selectedAllergens An optional List of Allergens selected for the dish. Can be null if no allergens are selected.
     * @param vegan A boolean indicating whether the dish is vegan.
     * @return The name of the view to render or a redirect URL.
     * @throws IOException If an I/O exception occurs while handling the uploaded image.
     */
    @PostMapping({"/menu/{id}/admin/edit-dish/save"})
    public String editDishProcess(Model model, Optional<Dish> dish, String ingredients, String action, @RequestParam(required = false) MultipartFile imageField, @RequestParam(required = false) List<Allergens> selectedAllergens, boolean vegan) throws IOException {

        Optional<Dish> finalDish = dishService.processEditDishInfo(dish, ingredients, action, selectedAllergens, vegan, imageField);

        if (finalDish.isPresent()) {
            model.addAttribute("dishId", finalDish.get().getId());

            String formAction = (finalDish.get().getId() != null) ? "/menu/" + finalDish.get().getId() + "/admin/edit-dish" : "/menu/admin/new-dish";
            model.addAttribute("formAction", formAction);

            if ("save".equals(action)) {
                dishService.save(finalDish.orElse(null));
                return "redirect:/menu/" + finalDish.get().getId();
            }
        }
        model.addAttribute("modalId", "confirmationModal");
        model.addAttribute("confirmButtonId", "confirmAction");
        model.addAttribute("modalMessage", "Are you sure you want to proceed with this action?");
        model.addAttribute("pageTitle", "Saved Changes");
        return "dish-form";
    }
    /**
     * Saves the rating for a dish.
     *
     * @param rating             The rating to be saved for the dish.
     * @param id                 The ID of the dish to be rated (optional).
     * @param redirectAttributes Attributes for flash messages to be passed to the view.
     * @return Redirects to the menu page of the rated dish.
     */
    @PostMapping("/menu/{id}/save-dish-rate")
    public String saveDishRate(@RequestParam("rating") int rating, @PathVariable(required = false) Long id, RedirectAttributes redirectAttributes) {
        dishService.setRating(id, rating);
        redirectAttributes.addFlashAttribute("mensaje", "Thanks for your rate!");
        return "redirect:/menu/" + id;
    }
}