package es.codeurjc.backend.service;

import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public void save(Dish dish){
        dishRepository.save(dish);
    }

}