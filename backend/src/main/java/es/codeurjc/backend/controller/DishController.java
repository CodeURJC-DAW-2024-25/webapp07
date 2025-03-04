package es.codeurjc.backend.controller;

import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.enums.Allergens;

import es.codeurjc.backend.model.User;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
                           Model model) throws SQLException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());

        model.addAttribute("isAuthenticated", isAuthenticated);

        List<Dish> dishes = dishService.filterDishes(name, ingredient, maxPrice);
        dishService.calculateRates(dishes);
        if (dishes.size() < 10){
            for (Dish dish : dishes) {
                dish.setDishImagePath(dish.blobToString(dish.getDishImagefile(), dish));
            }
        }else{
            for (int i = 0; i < 10; i++) {
                dishes.get(i).setDishImagePath(dishes.get(i).blobToString(dishes.get(i).getDishImagefile(), dishes.get(i)));
            }
        }

        model.addAttribute("dish", dishes.subList(0, Math.min(10, dishes.size())));

        model.addAttribute("pageTitle", "Menu");
        model.addAttribute("menuActive", true);

        return "menu";
    }

    @GetMapping("/api/menu")
    @ResponseBody
    public Map<String, Object> getDishes(@RequestParam(required = false) String name,
                                @RequestParam(required = false) String ingredient,
                                @RequestParam(required = false) Integer maxPrice,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int pageSize) throws SQLException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());


        System.out.println("NAME: "+ name + "INGREDIENT: " + ingredient + "MAXPRICE: " + maxPrice);

        List<Dish> filteredDishes = dishService.filterDishes(name, ingredient, maxPrice);
        filteredDishes = dishService.calculateRates(filteredDishes);

        for (Dish dish : filteredDishes) {
            System.out.println("NOMBRE:" + dish.getName() + " PRECIO:" + dish.getPrice());
        }


        // Paginar la lista filtrada manualmente
        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, filteredDishes.size());

        List<Dish> paginatedDishes = (fromIndex >= filteredDishes.size()) ?
                List.of() :
                filteredDishes.subList(fromIndex, toIndex);

        for (Dish dish : paginatedDishes) {
            if (dish.getImage()){
                dish.setDishImagePath(dish.blobToString(dish.getDishImagefile(), dish));
            }
        }

        // Crear respuesta en un mapa JSON
        Map<String, Object> response = new HashMap<>();
        response.put("isAuthenticated", isAuthenticated);
        response.put("dishes", paginatedDishes);

        return response;
    }

    @GetMapping("/menu/{id}")
    public String showDishInfo(Model model, @PathVariable long id) throws SQLException {

        Optional<Dish> dish = dishService.findById(id);
        if (dish.isPresent()) {
            if (dish.get().getImage()) {
                dish.get().setDishImagePath(dish.get().blobToString(dish.get().getDishImagefile(), dish.get()));
            }
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
            model.addAttribute("menuActive", true);

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

        String formAction;
        if (id != null) { // Modo edición
            model.addAttribute("pageTitle", "Edit dish");
            model.addAttribute("menuActive", true);
            Optional<Dish> dishOpt = dishService.findById(id);
            if (dishOpt.isPresent()) {
                Dish dish = new Dish();
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


    @PostMapping({"/menu/admin/new-dish/save", "/menu/{id}/admin/edit-dish/save"})
    public String editDishProcess(Model model, Dish dish, String ingredients, String action, @RequestParam(required = false) MultipartFile imageField, @RequestParam(required = false) List<Allergens> selectedAllergens, boolean vegan) throws IOException {
        List<String> ingredientsList = Arrays.stream(ingredients.toLowerCase().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        dish.setIngredients(ingredientsList);

        dish.setAllergens(selectedAllergens);
        dish.setVegan(vegan);

        if (imageField != null && !imageField.isEmpty()) {
            dish.setDishImagefile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
            dish.setImage(true);
        }

        model.addAttribute("dishId", dish.getId());

        String formAction = (dish.getId() != null) ? "/menu/" + dish.getId() + "/admin/edit-dish" : "/menu/admin/new-dish";
        model.addAttribute("formAction", formAction);

        if ("save".equals(action)) {
            dishService.save(dish);
            return "redirect:/menu/" + dish.getId();
        }

        model.addAttribute("modalId", "confirmationModal");
        model.addAttribute("confirmButtonId", "confirmAction");
        model.addAttribute("modalMessage", "Are you sure you want to proceed with this action?");

        model.addAttribute("pageTitle", "Saved Changes");
        return "dish-form";
    }
}