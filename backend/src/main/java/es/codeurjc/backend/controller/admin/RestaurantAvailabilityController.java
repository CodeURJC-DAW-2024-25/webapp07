package es.codeurjc.backend.controller.admin;

import es.codeurjc.backend.model.Restaurant;
import es.codeurjc.backend.service.BookingService;
import es.codeurjc.backend.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RestaurantAvailabilityController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private BookingService bookingService;

    // RestaurantAvailabilityController.java
    @GetMapping("/admin/restaurant-availability")
    public String showRestaurantAvailability(Model model) {
        List<Restaurant> restaurants = restaurantService.findAll();
        List<RestaurantAvailability> availabilityList = restaurants.stream()
                .map(restaurant -> new RestaurantAvailability(
                        restaurant.getLocation(),
                        bookingService.getAvailableSeats(restaurant.getId(), LocalDate.now(), "LUNCH")
                ))
                .collect(Collectors.toList());

        model.addAttribute("availabilityList", availabilityList);
        return "/admin/restaurant-availability";
    }

    public static class RestaurantAvailability {
        private String location;
        private int availableSeats;

        public RestaurantAvailability(String location, int availableSeats) {
            this.location = location;
            this.availableSeats = availableSeats;
        }

        public String getLocation() {
            return location;
        }

        public int getAvailableSeats() {
            return availableSeats;
        }
    }
}