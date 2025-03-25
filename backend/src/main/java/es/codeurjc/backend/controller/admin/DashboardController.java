package es.codeurjc.backend.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.codeurjc.backend.dto.DishDTO;
import es.codeurjc.backend.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private DishService dishService;

    @GetMapping("/admin/dashboard")
    public String showDashboard(Model model) {
        List<DishDTO> topDishes = dishService.getTop5DishesByRatingPriceRatio();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(topDishes);
            model.addAttribute("topDishes", json);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "admin/dashboard";
    }
}






