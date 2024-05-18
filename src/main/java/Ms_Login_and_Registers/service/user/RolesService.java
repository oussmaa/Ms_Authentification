package Ms_Login_and_Registers.service.user;


import Ms_Login_and_Registers.dto.response.user.RolesRequest;
import Ms_Login_and_Registers.models.Permissions;
import Ms_Login_and_Registers.models.Roles;
import Ms_Login_and_Registers.repository.PermisionRepository;
import Ms_Login_and_Registers.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RolesService {

    @Autowired
    private RolesRepository roleRepository;

    @Autowired
    private PermisionRepository permissionRepository;



    public List<Roles> getAllRoles() {
        return roleRepository.findAll();
    }

    public Roles getRoleById(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow();
    }

    public Long createRole(RolesRequest rolesRequest) {
            Roles role = new Roles();
            role.setRoles(rolesRequest.getRoles());

            Set<String> permissionNames = rolesRequest.getPermissions();
        for (String permissionName : permissionNames) {
            Permissions permission = permissionRepository.findByCode(permissionName);
            if (permission == null) {
                permission = new Permissions();
                permission.setCode(permissionName);
                permission = permissionRepository.save(permission);
            }
            role.getPermissions().add(permission);
        }


        role = roleRepository.save(role);

        // Get the ID of the saved role
        Long roleId = role.getId();
        return roleId;

    }

public Roles convertToEntity(RolesRequest rolesRequest)
{
    Roles roles = new Roles();
    roles.setDescrption(rolesRequest.getDescrption());
    roles.setRoles(rolesRequest.getRoles());
   // roles.setPermissions(rolesRequest.getPermissions());
   /* roles.setPermissions(rolesRequest.getPermissions().stream().map(perm -> {
        Permissions permissions = new Permissions();
        permissions.setCode(perm.getCode());
        return permissions;
    }).collect(Collectors.toList()));*/

return roles;
}
    public Roles updateRole(Long roleId, Roles updatedRole) {
        Roles role = roleRepository.findById(roleId)
                .orElseThrow();

        // Update the role fields with the values from updatedRole
        role.setRoles(updatedRole.getRoles());
        role.setDescrption(updatedRole.getDescrption());

        // Save the updated role
        return roleRepository.save(role);
    }

    public void deleteRole(Long roleId) {
        Roles role = roleRepository.findById(roleId)
                .orElseThrow();
        roleRepository.delete(role);
    }

}
