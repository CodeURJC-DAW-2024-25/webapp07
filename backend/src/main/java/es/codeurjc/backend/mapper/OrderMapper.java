package es.codeurjc.backend.mapper;

import es.codeurjc.backend.dto.DishDTO;
import es.codeurjc.backend.dto.OrderDTO;
import es.codeurjc.backend.model.Order;
import es.codeurjc.backend.model.Dish;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDTO toDto(Order order) {
        List<DishDTO> dishDTOs = order.getDishes().stream()
                .map(dish -> new DishDTO(dish.getId(), dish.getName(), dish.getPrice()))
                .collect(Collectors.toList());

        return new OrderDTO(
                order.getId(),
                dishDTOs,
                order.getUser().getId(),
                order.getOrderDate(),
                order.getAddress(),
                order.getStatus(),
                order.getTotalPrice()
        );
    }
}
