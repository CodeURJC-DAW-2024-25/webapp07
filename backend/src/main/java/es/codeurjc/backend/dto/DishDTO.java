package es.codeurjc.backend.dto;

import es.codeurjc.backend.enums.Allergens;

import java.sql.Blob;
import java.util.List;

public class DishDTO {
    private Long id;
    private String name;
    private String description;
    private int price;
    private List<String> ingredients;
    private List<Allergens> allergens;
    private boolean isVegan;
    private Blob dishImagefile;
    private boolean image;
    private String dishImagePath;
    private boolean isAvailable;
    private List<Integer> rates;
    private int rate;
}
