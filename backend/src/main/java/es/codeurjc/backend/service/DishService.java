package es.codeurjc.backend.service;

import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing dishes.
 */
@Service
public class DishService {
    @Autowired
    private DishRepository dishRepository;

    /*
     * This method was previously used to create a new dish or update an existing one.
     * It is currently commented out.
     */
    /*public void newDish(String name, String description, int price, String ingredients, boolean isVegan){
        Dish dish = dishRepository.findTournamentByName(name);
        if (dish!=null){
            dish.setDescription(description);
            dish.setPrice(price);
            dishRepository.save(dish);
        }else {
            dishRepository.save(new Dish(name, description, price, ingredients, isVegan, null));
        }
    }*/

    /**
     * Filters dishes based on name, ingredient, and maximum price.
     *
     * @param name the name of the dish (optional)
     * @param ingredient an ingredient present in the dish (optional)
     * @param maxPrice the maximum price of the dish (optional)
     * @return a filtered list of dishes
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
     * @return a list of all dishes
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

//    public List<Dish> findByName(String query) {
//        return dishRepository.findDishByName(query);
//    }

//    public List<Dish> findByIngredient(String ingredient) {
//        return dishRepository.findDishByIngredients(ingredient);
//    }

//    public List<Dish> findBymaxPrice(int maxPrice) {
//        return dishRepository.findDishBymaxPrice(maxPrice);
//    }

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