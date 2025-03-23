package es.codeurjc.backend.mapper;

import es.codeurjc.backend.dto.BookingDTO;
import es.codeurjc.backend.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper extends EntityMapper<BookingDTO, Booking> {

    @Override
    @Mapping(source = "restaurant.id", target = "restaurantId")
    @Mapping(source = "user.id", target = "userId")
    BookingDTO toDto(Booking booking);

    @Override
    @Mapping(target = "restaurant.id", source = "restaurantId")
    @Mapping(target = "user.id", source = "userId")
    Booking toEntity(BookingDTO bookingDTO);
}
