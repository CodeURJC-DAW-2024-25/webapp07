package es.codeurjc.backend.mapper;

import es.codeurjc.backend.dto.UserDTO;
import es.codeurjc.backend.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, User> {
}
