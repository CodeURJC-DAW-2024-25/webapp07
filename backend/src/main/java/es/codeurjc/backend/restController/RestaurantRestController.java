package es.codeurjc.backend.restController;

import es.codeurjc.backend.dto.RestaurantDTO;
import es.codeurjc.backend.mapper.RestaurantMapper;
import es.codeurjc.backend.model.Restaurant;
import es.codeurjc.backend.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@Tag(name = "Restaurants", description = "Restaurant management REST API")
@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantRestController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantMapper restaurantMapper;

    @Operation(summary = "Get all restaurants")
    @GetMapping
    public List<RestaurantDTO> findAll() {
        return restaurantService.findAll().stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    @Operation(summary = "Get a restaurant by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant found"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> findById(@PathVariable Long id) {
        return restaurantService.findById(id)
                .map(restaurantMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new restaurant")
    @PostMapping
    public ResponseEntity<RestaurantDTO> create(@Valid @RequestBody RestaurantDTO dto) {
        Restaurant restaurant = restaurantMapper.toEntity(dto);
        restaurantService.save(restaurant);
        URI location = fromCurrentRequest().path("/{id}")
                .buildAndExpand(restaurant.getId()).toUri();
        return ResponseEntity.created(location).body(restaurantMapper.toDto(restaurant));
    }

    @Operation(summary = "Delete a restaurant")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Restaurant> restaurant = restaurantService.findById(id);
        if (restaurant.isPresent()) {
            restaurantService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update a restaurant")
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> update(@PathVariable Long id, @Valid @RequestBody RestaurantDTO dto) {
        if (restaurantService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Restaurant updated = restaurantMapper.toEntity(dto);
        updated.setId(id);
        restaurantService.save(updated);
        return ResponseEntity.ok(restaurantMapper.toDto(updated));
    }
}