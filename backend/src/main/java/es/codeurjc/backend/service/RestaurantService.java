package es.codeurjc.backend.service;

import es.codeurjc.backend.model.Restaurant;
import es.codeurjc.backend.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

/**
 * Service class for handling restaurant-related business logic.
 * Provides methods to retrieve, search, and normalize restaurant data.
 */
@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    /**
     * Retrieves all restaurants from the repository.
     *
     * @return A list of all restaurants.
     */
    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    /**
     * Retrieves a restaurant by its ID.
     *
     * @param id The ID of the restaurant to retrieve.
     * @return An Optional containing the restaurant if found, or empty if not.
     */
    public Optional<Restaurant> findById(Long id) {
        return restaurantRepository.findById(id);
    }

    /**
     * Retrieves a list of restaurants whose location contains the given text.
     * The location text is normalized before the search.
     *
     * @param location The location text to search for.
     * @return A list of restaurants that match the search criteria.
     */
    public List<Restaurant> findByLocationContaining(String location) {
        return restaurantRepository.findByLocationContaining(normalizeText(location));
    }

    /**
     * Normalizes the input text by removing accents and special characters.
     * This is useful for searching text in a more consistent way.
     *
     * @param text The text to normalize.
     * @return The normalized text without accents and special characters.
     */
    public static String normalizeText(String text) {
        if (text == null) return null;
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        return normalized;
    }
}