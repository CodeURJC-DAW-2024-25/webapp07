package es.codeurjc.backend.mapper;

import es.codeurjc.backend.dto.BookingDTO;
import es.codeurjc.backend.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between {@link Booking} entities and {@link BookingDTO} objects.
 * This interface uses MapStruct for automatic mapping.
 */
@Mapper(componentModel = "spring")
public interface BookingMapper extends EntityMapper<BookingDTO, Booking> {

    /**
     * Converts a {@link Booking} entity to its corresponding {@link BookingDTO}.
     *
     * @param booking the booking entity to convert
     * @return the converted BookingDTO
     */
    @Override
    @Mapping(source = "restaurant.id", target = "restaurantId")
    @Mapping(source = "user.id", target = "userId")
    BookingDTO toDto(Booking booking);

    /**
     * Converts a {@link BookingDTO} to its corresponding {@link Booking} entity.
     * Only IDs of user and restaurant are mapped here.
     *
     * @param bookingDTO the DTO to convert
     * @return the converted Booking entity
     */
    @Override
    @Mapping(target = "restaurant.id", source = "restaurantId")
    @Mapping(target = "user.id", source = "userId")
    Booking toEntity(BookingDTO bookingDTO);
}
