package es.codeurjc.backend.controller.admin;

import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for handling the admin dashboard view.
 * Provides the main interface for administrators to access dashboard-related pages.
 */
@Controller
public class DashboardController {

    /**
     * Displays the admin dashboard page.
     *
     * @param model The model to pass attributes to the view.
     * @return The view name for the admin dashboard.
     */
    @GetMapping("/admin/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("pageTitle", "Admin Dashboard");
        return "admin/dashboard";
    }
}

/**
 * REST Controller for providing dashboard-related data via API.
 * Handles data requests such as retrieving the top-rated dishes.
 */
@RestController
@RequestMapping("/api/dashboard")
class DashboardApiController {

    @Autowired
    private DishService dishService;

    /**
     * Retrieves a list of the top-rated dishes based on a stars-to-price ratio.
     * The ratio is calculated as the average rating divided by the dish price.
     * If the dish has no user ratings, the predefined rate value is used instead.
     *
     * @return A list of up to 5 top-rated dishes sorted in descending order of rating efficiency.
     */
    @GetMapping("/top-dishes")
    public List<DishDto> getTopDishes() {
        List<Dish> dishes = dishService.findAll();
        List<DishDto> dtoList = new ArrayList<>();

        for (Dish dish : dishes) {
            // Calculate the average rating from user rates if available
            double avgRating = 0;
            if (dish.getRates() != null && !dish.getRates().isEmpty()) {
                avgRating = dish.getRates().stream()
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0);
            } else {
                // Use the stored rate value if no user ratings exist
                avgRating = dish.getRate();
            }

            // Compute rating-to-price ratio, ensuring no division by zero
            double ratio = (dish.getPrice() > 0) ?
                    (avgRating / dish.getPrice()) * 100 : 0;

            dtoList.add(new DishDto(dish.getName(), ratio));
        }

        // Sort the dishes by their rating-to-price ratio in descending order
        dtoList.sort((a, b) -> Double.compare(b.getStarsToPriceRatio(), a.getStarsToPriceRatio()));

        // Return the top 5 dishes or fewer if there are not enough entries
        return dtoList.size() > 5 ? dtoList.subList(0, 5) : dtoList;
    }

    /**
     * Data Transfer Object (DTO) for representing dish statistics.
     * Stores the dish name and its stars-to-price ratio.
     */
    public static class DishDto {
        private String name;
        private double starsToPriceRatio;

        /**
         * Constructs a DishDto object with dish name and rating-to-price ratio.
         *
         * @param name              The name of the dish.
         * @param starsToPriceRatio The ratio of stars to price, representing value efficiency.
         */
        public DishDto(String name, double starsToPriceRatio) {
            this.name = name;
            this.starsToPriceRatio = starsToPriceRatio;
        }

        /**
         * Gets the name of the dish.
         *
         * @return The dish name.
         */
        public String getName() {
            return name;
        }

        /**
         * Gets the rating-to-price ratio of the dish.
         *
         * @return The calculated stars-to-price ratio.
         */
        public double getStarsToPriceRatio() {
            return starsToPriceRatio;
        }
    }
}
