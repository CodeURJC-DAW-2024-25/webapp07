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

@Service
public class DishService {
    @Autowired
    private DishRepository dishRepository;

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

    public List<Dish> findAll() {
        return dishRepository.findAll();
    }

    public void save(Dish dish){
        dishRepository.save(dish);
    }

    public Optional<Dish> findById(long id) {
        return dishRepository.findById(id);

    }
    public void deleteById(long id) {
        dishRepository.deleteById(id);
    }

    public Page<Dish> findAllDishes(PageRequest pageRequest) {
        return dishRepository.findAll(pageRequest);
    }

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

    public List<Dish> searchDishes(String query) {
        return dishRepository.findByNameContainingIgnoreCase(query);
    }

    public List<Dish> calculateRates(List<Dish> dishes) {
        for (Dish dish:dishes){
            int rate =  (int) Math.ceil(dish.getRates().stream().mapToInt(Integer::intValue).average().orElse(0));
            dish.setRate(rate);
            save(dish);
        }
        return dishes;

    }
}