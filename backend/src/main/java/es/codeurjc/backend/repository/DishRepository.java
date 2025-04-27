package es.codeurjc.backend.repository;

import es.codeurjc.backend.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Repository interface for managing Dish entities.
 * Provides methods to perform queries on the Dish database table.
 */
@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    @Query("SELECT d FROM Dish d WHERE " +
            "(:name IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:maxPrice IS NULL OR d.price <= :maxPrice) AND " +
            "(:ingredient IS NULL OR :ingredient MEMBER OF d.ingredients)")
    Page<Dish> findFiltered(
            @Param("name") String name,
            @Param("maxPrice") Double maxPrice,
            @Param("ingredient") String ingredient,
            Pageable pageable);


    /**
     * Finds dishes by their exact name.
     *
     * @param dishName The name of the dish to search for.
     * @return A list of dishes matching the given name.
     */
    @Query("SELECT d FROM Dish d WHERE d.name = :dishName")
    List<Dish> findDishByName(String dishName);

    /**
     * Finds dishes that contain a specific ingredient.
     *
     * @param ingredient The ingredient to search for.
     * @return A list of dishes that include the given ingredient.
     */
    @Query("SELECT d FROM Dish d JOIN d.ingredients i WHERE LOWER(i) LIKE LOWER(CONCAT('%', :ingredient, '%'))")
    List<Dish> findDishByIngredients(String ingredient);

    /**
     * Finds dishes with a price less than or equal to the specified maximum price.
     *
     * @param maxPrice The maximum price of the dish.
     * @return A list of dishes that have a price less than or equal to maxPrice.
     */
    @Query("SELECT d FROM Dish d WHERE d.price <= :maxPrice")
    List<Dish> findDishBymaxPrice(int maxPrice);

    /**
     * Finds dishes whose names contain the given string, ignoring case.
     *
     * @param name The partial name to search for.
     * @return A list of dishes with names that contain the specified string.
     */
    List<Dish> findByNameContainingIgnoreCase(String name);
}
