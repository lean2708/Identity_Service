package devteria.identity_service.service;

import devteria.identity_service.dto.request.RoleRequest;
import devteria.identity_service.dto.response.RoleResponse;
import devteria.identity_service.entity.Permission;
import devteria.identity_service.mapper.RoleMapper;
import devteria.identity_service.repository.PermissionRepository;
import devteria.identity_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionRepository permissionRepository;


    public RoleResponse create(RoleRequest request){
        var role = roleMapper.toRole(request);

         List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
         role.setPermissions(new HashSet<>(permissions));

         role = roleRepository.save(role);
         return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll(){
        var roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(String role){
        roleRepository.deleteById(role);
    }
}
