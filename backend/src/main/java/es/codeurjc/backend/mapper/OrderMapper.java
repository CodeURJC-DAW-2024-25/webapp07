package es.codeurjc.backend.mapper;

import es.codeurjc.backend.dto.DishDTO;
import es.codeurjc.backend.dto.OrderDTO;
import es.codeurjc.backend.dto.UserDTO;
import es.codeurjc.backend.model.Order;
import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.model.User;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {

}
