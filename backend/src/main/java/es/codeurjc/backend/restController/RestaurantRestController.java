package es.codeurjc.backend.restController;

import es.codeurjc.backend.dto.RestaurantDTO;
import es.codeurjc.backend.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

/**
 * REST Controller for handling restaurant-related operations.
 * Exposes the same functionalities as the non-REST controller for restaurants.
 */
@Tag(name = "Restaurants", description = "Restaurant management REST API")
@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantRestController {

    @Autowired
    private RestaurantService restaurantService;

    /**
     * Retrieves a list of all restaurants or filters them by location based on a query parameter.
     *
     * @param query The location query parameter to filter restaurants.
     * @return A list of restaurants matching the filter.
     */
    @Operation(summary = "Get all restaurants or search by location")
    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants(@RequestParam(value = "query", required = false) String query) {
        List<RestaurantDTO> restaurants;

        if (query != null && !query.isEmpty()) {
            restaurants = restaurantService.findByLocationContaining(query);
        } else {
            restaurants = restaurantService.findAll();
        }

        return ResponseEntity.ok(restaurants);
    }

    @Operation(summary = "Get a restaurant by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant found"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> findById(@PathVariable Long id) {
        return restaurantService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new restaurant")
    @PostMapping
    public ResponseEntity<RestaurantDTO> create(@RequestBody RestaurantDTO dto) {
        RestaurantDTO createdRestaurant = restaurantService.createRestaurant(dto);
        URI location = fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdRestaurant.id()).toUri();
        return ResponseEntity.created(location).body(createdRestaurant);
    }

    @Operation(summary = "Update a restaurant")
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> update(@PathVariable Long id, @RequestBody RestaurantDTO dto) {
        try {
            RestaurantDTO updatedRestaurant = restaurantService.updateRestaurant(id, dto);
            return ResponseEntity.ok(updatedRestaurant);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a restaurant")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            restaurantService.deleteRestaurant(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
