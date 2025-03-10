package es.codeurjc.backend.service;

import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing dishes.
 */
@Service
public class DishService {
    @Autowired
    private DishRepository dishRepository;
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
    public void deleteById(long id) {
        dishRepository.deleteById(id);
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
}