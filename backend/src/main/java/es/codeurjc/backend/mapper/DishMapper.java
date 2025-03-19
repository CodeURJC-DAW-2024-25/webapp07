package es.codeurjc.backend.mapper;

import es.codeurjc.backend.dto.DishDTO;
import es.codeurjc.backend.model.Dish;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DishMapper extends EntityMapper<DishDTO, Dish> {
}
