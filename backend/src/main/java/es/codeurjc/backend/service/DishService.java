package es.codeurjc.backend.service;

import es.codeurjc.backend.dto.DishDTO;
import es.codeurjc.backend.enums.Allergens;
import es.codeurjc.backend.mapper.DishMapper;
import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.repository.DishRepository;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing dishes.
 */
@Service
public class DishService {
    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private DishMapper dishMapper;

    public List<Dish> processDishes( List<Dish> dishes) throws SQLException {
        if (dishes.size() < 10) {
            for (Dish dish : dishes) {
                int rate = (int) Math.ceil(dish.getRates().stream().mapToInt(Integer::intValue).average().orElse(0));
                dish.setRate(rate);
                dish.setDishImagePath(dish.blobToString(dish.getDishImagefile(), dish));
            }
        } else{
            for (int i = 0; i < 10; i++) {
                int rate =  (int) Math.ceil(dishes.get(i).getRates().stream().mapToInt(Integer::intValue).average().orElse(0));
                dishes.get(i).setRate(rate);
                dishes.get(i).setDishImagePath(dishes.get(i).blobToString(dishes.get(i).getDishImagefile(), dishes.get(i)));
            }
        }
        return dishes;
    }
    /**
     * Filters dishes based on the provided name, ingredient, and maximum price.
     *
     * @param name The name of the dish to filter by (optional).
     * @param ingredient The ingredient to filter dishes by (optional).
     * @param maxPrice The maximum price to filter dishes by (optional).
     * @return A list of dishes that match the provided filters and are available.
     */
    public List<Dish> filterDishes(String name, String ingredient, Integer maxPrice) {

        List<Dish> dishesByName =  (name != null  && !name.isEmpty())
                ? dishRepository.findDishByName(name)
                : dishRepository.findAll();

        List<Dish> dishesByIngredient = (ingredient != null && !ingredient.isEmpty())
                ? dishRepository.findDishByIngredients(ingredient)
                : dishRepository.findAll();

        List<Dish> dishesByPrice = (maxPrice != null)
                ? dishRepository.findDishBymaxPrice(maxPrice)
                : dishRepository.findAll();

        // More than 1 filter
        if (name != null && !name.isEmpty()) {
            dishesByName.retainAll(dishesByIngredient);
            dishesByName.retainAll(dishesByPrice);
            dishesByName.removeIf(dish -> !dish.isAvailable());
            return dishesByName;  // Final list
        } else {
            dishesByIngredient.retainAll(dishesByPrice);
            dishesByIngredient.removeIf(dish -> !dish.isAvailable());

            return dishesByIngredient;  // Final list
        }
    }
    /**
     * Retrieves all dishes from the repository.
     *
     * @return A list of all dishes.
     */
    public List<Dish> findAll() {
        return dishRepository.findAll();
    }

    /**
     * Saves a dish to the repository.
     *
     * @param dish the dish to save
     */
    public void save(Dish dish){
        dishRepository.save(dish);
    }

    /**
     * Finds a dish by its ID.
     *
     * @param id the ID of the dish
     * @return an Optional containing the dish if found
     */
    public Optional<Dish> findById(long id) {
        return dishRepository.findById(id);
    }

    /**
     * Deletes a dish by its ID.
     *
     * @param id the ID of the dish to delete
     */
    public DishDTO deleteById(long id) {
        Optional<Dish> dish = dishRepository.findById(id);
        dishRepository.deleteById(id);
        return dish.map(dishMapper::toDto).orElse(null);
    }

    /**
     * Retrieves a paginated list of dishes.
     *
     * @param pageRequest the pagination request
     * @return a page of dishes
     */
    public Page<Dish> findAllDishes(PageRequest pageRequest) {
        return dishRepository.findAll(pageRequest);
    }

    /**
     * Retrieves a paginated list of dishes with filters.
     *
     * @param pageRequest the pagination request
     * @return a page of dishes with filters applied
     */

    public Page<Dish> findAllDishesByFilter(PageRequest pageRequest) {
        return dishRepository.findAll(pageRequest);
    }
    /**
     * Searches for dishes by name, ignoring case.
     *
     * @param query the name or partial name of the dish
     * @return a list of matching dishes
     */
    public List<Dish> searchDishes(String query) {
        return dishRepository.findByNameContainingIgnoreCase(query);
    }

    /**
     * Calculates and sets the average rating for each dish.
     *
     * @param dishes the list of dishes
     * @return the updated list of dishes with calculated ratings
     */
    public List<Dish> calculateRates(List<Dish> dishes) {
        for (Dish dish : dishes) {
            int rate = (int) Math.ceil(dish.getRates().stream().mapToInt(Integer::intValue).average().orElse(0));
            dish.setRate(rate);
            save(dish);
        }
        return dishes;
    }
    /**
     * Disables a dish by its unique identifier.
     *
     * @param id The unique identifier of the dish to disable.
     * @throws IllegalArgumentException If no dish is found with the given ID in the repository.
     */
    public void disableById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found"));
        dish.setAvailable(false);
        dishRepository.save(dish);
    }
    /**
     * Enables a dish by its unique identifier.
     *
     * @param id The unique identifier of the dish to enable.
     * @throws IllegalArgumentException If no dish is found with the given ID in the repository.
     */
    public void enableById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found"));
        dish.setAvailable(true);
        dishRepository.save(dish);
    }
    /**
     * Creates and associates an image with a specific dish.
     *
     * @param id The unique identifier of the dish to associate the image with.
     * @param inputStream The input stream containing the image data.
     * @param size The size of the image data in bytes.
     * @throws IllegalArgumentException If no dish is found with the given ID.
     */
    public void createDishImage(long id, URI location, InputStream inputStream, long size) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found"));
        dish.setDishImagePath(location.toString());
        dish.setDishImagefile(BlobProxy.generateProxy(inputStream, size));
        dish.setImage(true);
        dishRepository.save(dish);
    }
    /**
     * Retrieves the image of a specific dish as a Spring Resource.
     *
     * @param id The unique identifier of the dish whose image is to be retrieved.
     * @return A Spring Resource representing the dish's image.
     * @throws IllegalArgumentException If no dish is found with the given ID.
     * @throws SQLException If an SQL exception occurs while accessing the image data.
     * @throws NoSuchElementException If the dish exists but does not have an associated image.
     */
    public Resource getDishImage(long id) throws SQLException {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found"));
        if (dish.getImage()) {
            return new InputStreamResource(dish.getDishImagefile().getBinaryStream());
        } else {
            throw new NoSuchElementException();
        }
    }
    /**
     * Replaces the existing image of a specific dish with a new one.
     *
     * @param id The unique identifier of the dish whose image is to be replaced.
     * @param inputStream The input stream containing the new image data.
     * @param size The size of the new image data in bytes.
     * @throws java.util.NoSuchElementException If no dish is found with the given ID,
     * or if the dish exists but does not have an image associated with it.
     */
    public void replaceDishImage(long id, InputStream inputStream, long size) {
        Dish dish = dishRepository.findById(id).orElseThrow();
        if (!dish.getImage()) {
            throw new NoSuchElementException();
        }
        dish.setDishImagefile(BlobProxy.generateProxy(inputStream, size));
        dishRepository.save(dish);
    }
    /**
     * Creates a new dish based on the provided DishDTO.
     *
     * @param dishDTO The DishDTO containing the information for the new dish.
     * The 'id' field in the DishDTO must be null, as this method is for creating new entities.
     * @return The DishDTO representing the newly created dish, including its generated ID.
     * @throws IllegalArgumentException If the provided DishDTO already has an ID set,
     * indicating that it's intended for an update rather than creation.
     */
    public DishDTO createDish(DishDTO dishDTO) {
        if(dishDTO.id() != null) {
            throw new IllegalArgumentException();
        }

        Dish dish = dishMapper.toEntity(dishDTO);

        dishRepository.save(dish);

        return dishMapper.toDto(dish);
    }

    /**
     * Generates a list representing filled stars based on a given rating.
     *
     * @param rate The integer representing the rating (number of filled stars).
     * @return A List of Integers, where the size of the list is equal to the 'rate'.
     * Each element in the list can be a placeholder value (e.g., 0) indicating a filled star.
     */
    public List<Integer> getStarList(int rate) {
        List<Integer> starList = new ArrayList<>();
        for (int i = 0; i < rate; i++){
            starList.add(0);
        }
        return starList;
    }
    /**
     * Generates a list representing empty stars to complement a given rating.
     *
     * @param rate The integer representing the number of filled stars.
     * @return A List of Integers, where the size of the list is the difference between 5 and the 'rate'.
     * Each element in the list can be a placeholder value (e.g., 0) indicating an empty star.
     */
    public List<Integer> getNoStarList(int rate) {
        List<Integer> noStarList = new ArrayList<>();
        for (int i =rate; i < 5; i++){
            noStarList.add(0);
        }
        return noStarList;
    }
    /**
     * Formats the ingredients of a dish by capitalizing the first letter of each ingredient
     * and converting the rest to lowercase.
     *
     * @param dish An Optional containing the Dish object whose ingredients need to be formatted.
     * @return A List of Strings, where each string is a formatted ingredient name.
     * @throws NoSuchElementException If the provided Optional is empty, indicating that the dish
     * does not exist.
     */
    public List<String> formatIngredients(Optional<Dish> dish) {
        if (dish.isPresent()){
            return dish.get().getIngredients().stream()
                    .map(ing -> ing.substring(0, 1).toUpperCase() + ing.substring(1).toLowerCase())
                    .toList();
        } else{
            throw new NoSuchElementException();
        }

    }

    public void setRating(Optional<Dish> dish, int rating) {
        if (dish.isPresent()){
            List<Integer> listRates = dish.get().getRates();
            listRates.add(rating);
            dish.get().setRates(listRates);
            dishRepository.save(dish.get());        }
    }

    public Optional<Dish> processNewDishInfo(Optional<Dish> dish, String ingredients, List<Allergens> selectedAllergens, boolean vegan, MultipartFile imageField) throws IOException {
        List<String> ingredientsList = Arrays.stream(ingredients.toLowerCase().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        if(dish.isPresent()) {
            dish.get().setIngredients(ingredientsList);

            dish.get().setAllergens(selectedAllergens);
            dish.get().setVegan(vegan);

            setImageFile(imageField, dish);
        }
        return dish;
    }

    public Optional<Dish> processEditDishInfo(Optional<Dish> dish, String ingredients, String action, List<Allergens> selectedAllergens, boolean vegan, MultipartFile imageField) throws IOException {
        Long id = null;
        if (dish.isPresent()) {
            id = dish.get().getId();
            Optional<Dish> originDish = dishRepository.findById(id);
            List<String> ingredientsList = Arrays.stream(ingredients.toLowerCase().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            dish.get().setIngredients(ingredientsList);

            if (originDish.isPresent()) {
                if (selectedAllergens != null) {
                    dish.get().setAllergens(selectedAllergens);
                } else {
                    dish.get().setAllergens(originDish.get().getAllergens());
                }

                if (vegan == originDish.get().isVegan()) {
                    dish.get().setVegan(vegan);
                } else {
                    dish.get().setAllergens(originDish.get().getAllergens());
                }

                setImageFile(imageField, dish);

                if (!setImageFile(imageField, dish)){
                    dish.get().setDishImagefile(originDish.get().getDishImagefile());
                    dish.get().setImage(true);
                }

                dish.get().setRates(originDish.get().getRates());
                dish.get().setAvailable(originDish.get().isAvailable());
            }
        }
        return dish;
    }

    private boolean setImageFile(MultipartFile imageField, Optional<Dish> dish) throws IOException {
        if (imageField != null && !imageField.isEmpty()) {
            if (dish.isPresent()) {
                dish.get().setDishImagefile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
                dish.get().setImage(true);
                return true;
            }
        }
        return false;
    }
}