package Ms_Login_and_Registers.controller.user;


import Ms_Login_and_Registers.dto.response.user.RolesRequest;
import Ms_Login_and_Registers.models.Roles;
import Ms_Login_and_Registers.service.user.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/RolesRequest")
@CrossOrigin(origins = "http://localhost:3000")
public class RolesController {



    @Autowired
    private RolesService roleService;

    @GetMapping("/getallroles")
    public ResponseEntity<List<Roles>> getAllRoles() {
        List<Roles> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/getrolesbyid/{roleId}")
    public ResponseEntity<Roles> getRoleById(@PathVariable Long roleId) {
        Roles role = roleService.getRoleById(roleId);
        return ResponseEntity.ok(role);
    }

    @PostMapping("/addroles")
    public ResponseEntity<RolesRequest> createRole(@RequestBody RolesRequest rolesRequest) {
        roleService.createRole(rolesRequest);
        return ResponseEntity.ok(rolesRequest);
    }

    @PutMapping("/updateroles/{roleId}")
    public ResponseEntity<Roles> updateRole(@PathVariable Long roleId, @RequestBody Roles updatedRole) {
        Roles role = roleService.updateRole(roleId, updatedRole);
        return ResponseEntity.ok(role);
    }

    @DeleteMapping("/delete/{roleId}")
    public ResponseEntity<String> deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.ok("Role deleted successfully");
    }
}




