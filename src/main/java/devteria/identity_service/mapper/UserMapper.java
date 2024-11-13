package devteria.identity_service.mapper;

import devteria.identity_service.dto.request.UserCreationRequest;
import devteria.identity_service.dto.request.UserUpdateRequest;
import devteria.identity_service.dto.response.UserResponse;
import devteria.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    public User toUser(UserCreationRequest request);

    @Mapping(target = "roles", ignore = true)
    public UserResponse toUserResponse(User user);

    public void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
