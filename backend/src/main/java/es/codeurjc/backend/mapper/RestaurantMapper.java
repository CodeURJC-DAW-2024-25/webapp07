package es.codeurjc.backend.mapper;

import es.codeurjc.backend.dto.RestaurantDTO;
import es.codeurjc.backend.model.Restaurant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantMapper extends EntityMapper<RestaurantDTO, Restaurant> {
}
