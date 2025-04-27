package es.codeurjc.backend.mapper;

import es.codeurjc.backend.dto.OrderDTO;
import es.codeurjc.backend.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = { DishMapper.class, UserMapper.class })
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {

    @Override
    @Mappings({
            @Mapping(source = "dishes", target = "dishes"),
            @Mapping(source = "user", target = "user")
    })
    OrderDTO toDto(Order entity);

    @Override
    @Mappings({
            @Mapping(source = "dishes", target = "dishes"),
            @Mapping(source = "user", target = "user")
    })
    Order toEntity(OrderDTO dto);
}
