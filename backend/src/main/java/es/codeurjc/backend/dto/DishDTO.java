package es.codeurjc.backend.dto;

import es.codeurjc.backend.enums.Allergens;

import java.sql.Blob;
import java.util.List;

public record DishDTO(
        Long id,
        String name,
        String description,
        int pric,
        List<String> ingredients,
        List<Allergens> allergens,
        boolean isVegan,
        Blob dishImagefile,
        boolean image,
        String dishImagePat,
        boolean isAvailable,
        List<Integer> rates,
        int rate
){};
