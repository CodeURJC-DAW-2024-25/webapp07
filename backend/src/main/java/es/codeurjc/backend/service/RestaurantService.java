package es.codeurjc.backend.service;

import es.codeurjc.backend.dto.RestaurantDTO;
import es.codeurjc.backend.mapper.RestaurantMapper;
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

    @Autowired
    private RestaurantMapper restaurantMapper;

    /**
     * Retrieves all restaurants from the repository.
     *
     * @return A list of all restaurants.
     */
    public List<RestaurantDTO> findAll() {
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    /**
     * Retrieves a restaurant by its ID.
     *
     * @param id The ID of the restaurant to retrieve.
     * @return An Optional containing the restaurant if found, or empty if not.
     */
    public Optional<RestaurantDTO> findById(Long id) {
        return restaurantRepository.findById(id)
                .map(restaurantMapper::toDto);
    }

    /**
     * Retrieves a list of restaurants whose location contains the given text.
     * The location text is normalized before the search.
     *
     * @param location The location text to search for.
     * @return A list of restaurants that match the search criteria.
     */
    public List<RestaurantDTO> findByLocationContaining(String location) {
        return restaurantRepository.findByLocationContaining(normalizeText(location)).stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    public RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO) {
        if (restaurantDTO.id() != null) {
            throw new IllegalArgumentException("New restaurant cannot have an ID");
        }
        Restaurant restaurant = restaurantMapper.toEntity(restaurantDTO);
        restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(restaurant);
    }

    public RestaurantDTO updateRestaurant(Long id, RestaurantDTO restaurantDTO) {
        if (!restaurantRepository.existsById(id)) {
            throw new IllegalArgumentException("Restaurant not found");
        }
        Restaurant restaurant = restaurantMapper.toEntity(restaurantDTO);
        restaurant.setId(id);
        restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(restaurant);
    }

    public void deleteRestaurant(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new IllegalArgumentException("Restaurant not found");
        }
        restaurantRepository.deleteById(id);
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