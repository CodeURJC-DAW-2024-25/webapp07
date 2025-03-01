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

    public List<Dish> findByName(String query) {
        return dishRepository.findDishByName(query);
    }

    public List<Dish> findByIngredient(String ingredient) {
        return dishRepository.findDishByIngredients(ingredient);
    }

    public List<Dish> findBymaxPrice(int maxPrice) {
        return dishRepository.findDishBymaxPrice(maxPrice);
    }

    public List<Dish> searchDishes(String query) {
        return dishRepository.findByNameContainingIgnoreCase(query);
    }
}