package es.codeurjc.backend.controller.admin;

import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.List;

// Split into two controllers: one for views, one for REST API
@Controller
public class DashboardController {

    @GetMapping("/admin/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("pageTitle", "Admin Dashboard");
        return "admin/dashboard"; // Remove leading slash
    }
}

@RestController
@RequestMapping("/api/dashboard")
class DashboardApiController {

    @Autowired
    private DishService dishService;

    @GetMapping("/top-dishes")
    public List<DishDto> getTopDishes() {
        List<Dish> dishes = dishService.findAll();
        List<DishDto> dtoList = new ArrayList<>();

        for (Dish dish : dishes) {
            // Calculate the average rating from the rates collection if available
            double avgRating = 0;
            if (dish.getRates() != null && !dish.getRates().isEmpty()) {
                avgRating = dish.getRates().stream()
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0);
            } else {
                // Use the stored rate value if rates collection is empty
                avgRating = dish.getRate();
            }

            // Calculate ratio with a larger multiplier (500 instead of 100)
            double ratio = (dish.getPrice() > 0) ?
                    (avgRating / dish.getPrice()) * 100 : 0;

            dtoList.add(new DishDto(dish.getName(), ratio));
        }

        // Sort by ratio in descending order
        dtoList.sort((a, b) -> Double.compare(b.getStarsToPriceRatio(), a.getStarsToPriceRatio()));

        // Return top 5 or fewer if not enough dishes
        return dtoList.size() > 5 ? dtoList.subList(0, 5) : dtoList;
    }

    public static class DishDto {
        private String name;
        private double starsToPriceRatio;

        public DishDto(String name, double starsToPriceRatio) {
            this.name = name;
            this.starsToPriceRatio = starsToPriceRatio;
        }

        public String getName() {
            return name;
        }

        public double getStarsToPriceRatio() {
            return starsToPriceRatio;
        }
    }
}