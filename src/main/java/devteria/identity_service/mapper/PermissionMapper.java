package devteria.identity_service.mapper;

import devteria.identity_service.dto.request.PermissionRequest;
import devteria.identity_service.dto.response.PermissionResponse;
import devteria.identity_service.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    public Permission toPermission(PermissionRequest request);

    public PermissionResponse toPermissionResponse(Permission permission);

    public void updatePermission(@MappingTarget Permission permission, PermissionRequest request);
}
