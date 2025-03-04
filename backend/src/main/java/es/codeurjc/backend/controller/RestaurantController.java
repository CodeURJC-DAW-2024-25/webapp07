package es.codeurjc.backend.controller;

import es.codeurjc.backend.model.Restaurant;
import es.codeurjc.backend.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/restaurants")
    public String showRestaurants(@RequestParam(value = "query", required = false) String query, Model model) {
        if (query != null && !query.isEmpty()) {
            try {
                query = URLDecoder.decode(query, StandardCharsets.UTF_8.name());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<Restaurant> restaurants = (query != null && !query.isEmpty())
                ? restaurantService.findByLocationContaining(query)
                : restaurantService.findAll();

        model.addAttribute("restaurants", restaurants);
        return "restaurants";
    }
}