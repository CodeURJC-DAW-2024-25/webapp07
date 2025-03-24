package es.codeurjc.backend.restController;
import es.codeurjc.backend.dto.DishDTO;
import es.codeurjc.backend.dto.UserDTO;
import es.codeurjc.backend.exception.custom.ResourceNotFoundException;
import es.codeurjc.backend.mapper.DishMapper;
import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@Tag(name = "Dishes", description = "Dish management REST API")
@RestController
@RequestMapping("/api/v1/dishes")
public class DishRestController {

    @Autowired
    private DishService dishService;

    @Operation(summary = "Get all dishes", description = "Returns a list of all dishes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dishes retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DishDTO.class)))
    })
    @GetMapping({"/", "/filter", "/sort"})
    public ResponseEntity<List<DishDTO>> showMenu() throws SQLException {
        List<DishDTO> dishes = dishService.findAll();
        return ResponseEntity.ok(dishes);
    }

    @Operation(summary = "Get a dish by ID", description = "Fetches a dish by their unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dish found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DishDTO.class))),
            @ApiResponse(responseCode = "404", description = "Dish not found"),
            @ApiResponse(responseCode = "500", description = "PAPA FRITASAJCNALNDLKASMDKSAMDLKMASDKLAMLDAS")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DishDTO> getDish(@PathVariable long id) {
        return dishService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Dish with id " + id + " not found"));
    }

    @Operation(summary = "Update a dish by ID", description = "Update a dish by their unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dish updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DishDTO.class))),
            @ApiResponse(responseCode = "404", description = "Dish not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DishDTO> updateDish(@PathVariable Long id, @Valid @RequestBody DishDTO dishDTO) {
        Optional<DishDTO> updatedDish = dishService.updateDish(id, dishDTO);
        return updatedDish.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Disable a dish", description = "Marks a dish as disable in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Dish disabled successfully"),
            @ApiResponse(responseCode = "404", description = "Dish not found")
    })
    @PatchMapping("/{id}/disabled")
    public ResponseEntity<Void> disableDish(@PathVariable Long id) {
        dishService.disableById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Enable a dish", description = "Marks a dish as enabled in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Dish enabled successfully"),
            @ApiResponse(responseCode = "404", description = "Dish not found")
    })
    @PatchMapping("/{id}/enabled")
    public ResponseEntity<Void> enableDish(@PathVariable Long id) {
        dishService.enableById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create a new dish", description = "Create a new dish at the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Dish created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DishDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"message\": \"Error at validation\", \"details\": [...]\"}"))),
            @ApiResponse(responseCode = "500", description = "System ",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"message\": \"Internal server error\"}")))
    })
    @PostMapping("/")
    public ResponseEntity<DishDTO> createDish(@Valid @RequestBody DishDTO dishDTO) {
        long dishId = dishService.createDish(dishDTO);
        URI location = URI.create("/api/v1/dishes/" + dishId);
        return ResponseEntity.created(location).body(dishDTO);
    }

    @Operation(summary = "Upload an image for a specific dish", description = "Uploads an image file and associates it with the dish identified by the given ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Image uploaded successfully",
                    headers = @io.swagger.v3.oas.annotations.headers.Header(name = "Location", description = "URI of the uploaded image resource")),
            @ApiResponse(responseCode = "400", description = "Invalid request - e.g., invalid file format or size"),
            @ApiResponse(responseCode = "404", description = "Dish not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error during image upload")
    })
    @PostMapping("/{id}/image")
    public ResponseEntity<Object> createDishImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
            throws IOException {

        URI location = fromCurrentRequest().build().toUri();

        dishService.createDishImage(id, location, imageFile.getInputStream(), imageFile.getSize());

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Retrieve the image for a specific dish", description = "Retrieves the image associated with the dish identified by the given ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Image retrieved successfully",
                    content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE)),
            @ApiResponse(responseCode = "404", description = "Dish or image not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error during image retrieval")
    })
    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getDishImage(@PathVariable long id) throws SQLException, IOException {

        Resource postImage = dishService.getDishImage(id);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(postImage);

    }

    @Operation(summary = "Replace the image for a specific dish", description = "Replaces the existing image associated with the dish identified by the given ID with a new image file.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Image replaced successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request - e.g., invalid file format or size"),
            @ApiResponse(responseCode = "404", description = "Dish not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error during image replacement")
    })
    @PutMapping("/{id}/image")
    public ResponseEntity<Object> replaceDishImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
            throws IOException {

        dishService.replaceDishImage(id, imageFile.getInputStream(), imageFile.getSize());

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search dishes by name",
            description = "Returns a list of dishes that contain the specified name."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Dishes found successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DishDTO.class)))
    )
    @GetMapping("/foundName")
    public ResponseEntity<List<DishDTO>> searchDishByName(
            @Parameter(description = "Search query for username or email") @RequestParam String query) {

        List<DishDTO> dishes = dishService.searchDishByName(query);
        return ResponseEntity.ok(dishes);
    }

    @Operation(
            summary = "Search dishes by ingredient",
            description = "Returns a list of dishes that include the specified ingredient."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Dishes found successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DishDTO.class)))
    )
    @GetMapping("/foundIngredient")
    public ResponseEntity<List<DishDTO>> searchByIngredient(
            @Parameter(description = "Search query for username or email") @RequestParam String query) {

        List<DishDTO> dishes = dishService.searchDishByIngredient(query);
        return ResponseEntity.ok(dishes);
    }

    @Operation(
            summary = "Search dishes by maximum price",
            description = "Returns a list of dishes with a price lower than or equal to the specified value."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Dishes found successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DishDTO.class)))
    )
    @GetMapping("/foundPrice")
    public ResponseEntity<List<DishDTO>> searchDishByMaxPrice(
            @Parameter(description = "Search query for username or email") @RequestParam Integer query) {

        List<DishDTO> dishes = dishService.searchDishByPrice(query);
        return ResponseEntity.ok(dishes);
    }
}
