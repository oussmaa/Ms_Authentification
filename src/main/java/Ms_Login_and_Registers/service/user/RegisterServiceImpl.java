package Ms_Login_and_Registers.service.user;

import Ms_Login_and_Registers.models.Permissions;
import Ms_Login_and_Registers.models.Roles;
import Ms_Login_and_Registers.repository.PermisionRepository;
import Ms_Login_and_Registers.repository.RolesRepository;
import Ms_Login_and_Registers.repository.UserRepository;
import Ms_Login_and_Registers.dto.request.user.RegisterRequest;
import Ms_Login_and_Registers.exception.InvalidRequestException;
import Ms_Login_and_Registers.models.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RolesRepository roleRepository;
    @Autowired
    PermisionRepository permissionRepository;

   @Autowired
   RolesService rolesService;

    PasswordEncoder encoder;


    @Override
    public void process(RegisterRequest request) throws InvalidRequestException {
            User user = new User();
            boolean usernameExists = userRepository.existsByUsername(request.getUsername());
            if (usernameExists) throw new InvalidRequestException("Username already exists");

            boolean emailExists = userRepository.existsByEmail(request.getEmail());
            if (emailExists) throw new InvalidRequestException("Email already exists");

            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setName(request.getName());
            user.setThemeid(request.getThemeid());
            user.setLocked(request.isLocked());
            user.setPhone(request.getPhone());
            user.setPassword(encoder.encode(request.getPassword()));
            user.setImages(request.getImages());
            user.setAdress(request.getAdress());


        // Set permissions
        Set<Permissions> permissions = new HashSet<>();
        for (String permissionName : request.getPermissionNames()) {
            Permissions permission = permissionRepository.findByCode(permissionName);
            if (permission != null) {
                permissions.add(permission);
            }
        }
        user.setPermissions(permissions);

            // Set roles
            Set<Roles> roles = new HashSet<>();

                Roles role = roleRepository.findByRoles(request.getRoles());
                if (role != null) {
                    roles.add(role);
                } else {
                    throw new InvalidRequestException("Role " + request.getRoles() + " not found");
                }

            user.setRoles(roles);

            userRepository.save(user);

    }



}
