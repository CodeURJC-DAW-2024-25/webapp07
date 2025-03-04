package es.codeurjc.backend.controller.admin;

import es.codeurjc.backend.model.Restaurant;
import es.codeurjc.backend.service.BookingService;
import es.codeurjc.backend.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RestaurantAvailabilityController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/admin/restaurant-availability")
    public String showRestaurantAvailability(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {
        if (date == null) {
            date = LocalDate.now();
        }

        final LocalDate finalDate = date;

        List<Restaurant> restaurants = restaurantService.findAll();
        List<RestaurantAvailability> availabilityList = restaurants.stream()
                .map(restaurant -> new RestaurantAvailability(
                        restaurant.getLocation(),
                        bookingService.getAvailableSeats(restaurant.getId(), finalDate, "LUNCH"),
                        bookingService.getAvailableSeats(restaurant.getId(), finalDate, "DINNER")
                ))
                .collect(Collectors.toList());

        model.addAttribute("availabilityList", availabilityList);
        model.addAttribute("selectedDate", finalDate);
        return "/admin/restaurant-availability";
    }

    public static class RestaurantAvailability {
        private String location;
        private int lunchAvailableSeats;
        private int dinnerAvailableSeats;

        public RestaurantAvailability(String location, int lunchAvailableSeats, int dinnerAvailableSeats) {
            this.location = location;
            this.lunchAvailableSeats = lunchAvailableSeats;
            this.dinnerAvailableSeats = dinnerAvailableSeats;
        }

        public String getLocation() {
            return location;
        }

        public int getLunchAvailableSeats() {
            return lunchAvailableSeats;
        }

        public int getDinnerAvailableSeats() {
            return dinnerAvailableSeats;
        }
    }
}