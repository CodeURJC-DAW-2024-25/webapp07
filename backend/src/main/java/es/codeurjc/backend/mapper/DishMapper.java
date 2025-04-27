package es.codeurjc.backend.mapper;

import es.codeurjc.backend.dto.DishDTO;
import es.codeurjc.backend.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DishMapper extends EntityMapper<DishDTO, Dish> {
    @Override
    @Mapping(source = "available", target = "available")  // Esto no es necesario si los nombres coinciden exactamente
    @Mapping(source = "price", target = "price")
    DishDTO toDto(Dish entity);

    @Override
    @Mapping(source = "available", target = "available")  // Para mapear correctamente entre los dos
    @Mapping(source = "price", target = "price")
    Dish toEntity(DishDTO dto);
}
