package es.codeurjc.backend.repository;

import es.codeurjc.backend.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Long> {
    @Query("SELECT d From Dish d WHERE d.name = :dishName")
    List<Dish> findDishByName(String dishName);

    @Query("SELECT d FROM Dish d JOIN d.ingredients i WHERE LOWER(i) LIKE LOWER(CONCAT('%', :ingredient, '%'))")
    List<Dish> findDishByIngredients(String ingredient);

    @Query("SELECT d From Dish d WHERE d.price <= :maxPrice")
    List<Dish> findDishBymaxPrice(int maxPrice);
}