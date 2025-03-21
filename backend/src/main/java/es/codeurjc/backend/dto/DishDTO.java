package es.codeurjc.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.codeurjc.backend.enums.Allergens;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Blob;
import java.util.List;

@Schema(description = "Represents a dish offered by the restaurant.")
public record DishDTO(

        @Schema(description = "Unique identifier of the dish", example = "1")
        Long id,

        @Schema(description = "Name of the dish", example = "Margherita Pizza")
        String name,

        @Schema(description = "Description of the dish", example = "Classic pizza with tomato, mozzarella and basil.")
        String description,

        @Schema(description = "Price of the dish in cents", example = "950")
        int pric,

        @Schema(description = "List of ingredients used in the dish", example = "[\"Tomato\", \"Mozzarella\", \"Basil\"]")
        List<String> ingredients,

        @Schema(description = "List of allergens present in the dish")
        List<Allergens> allergens,

        @Schema(description = "Indicates if the dish is vegan", example = "false")
        boolean isVegan,

        @JsonIgnore
        @Schema(hidden = true)
        Blob dishImagefile,

        @Schema(description = "Indicates whether the dish has an image", example = "true")
        boolean image,

        @Schema(description = "Path to the dish image if available", example = "/images/dishes/1.jpg")
        String dishImagePath,

        @Schema(description = "Availability status of the dish", example = "true")
        boolean isAvailable,

        @Schema(description = "List of user ratings for the dish", example = "[5, 4, 5, 3]")
        List<Integer> rates,

        @Schema(description = "Average rating of the dish", example = "4")
        int rate

) {}
