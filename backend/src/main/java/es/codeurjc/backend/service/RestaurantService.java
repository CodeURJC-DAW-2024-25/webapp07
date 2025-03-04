package es.codeurjc.backend.service;

import es.codeurjc.backend.model.Restaurant;
import es.codeurjc.backend.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    // Obtener todos los restaurantes disponibles
    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    // Buscar un restaurante por ID
    public Optional<Restaurant> findById(Long id) {
        return restaurantRepository.findById(id);
    }

    public List<Restaurant> findByLocationContaining(String location) {
        return restaurantRepository.findByLocationContaining(normalizeText(location));
    }

    public static String normalizeText(String text) {
        if (text == null) return null;
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        return normalized;
    }
}
