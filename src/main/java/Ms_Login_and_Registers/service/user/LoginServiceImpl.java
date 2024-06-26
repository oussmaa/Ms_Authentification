package Ms_Login_and_Registers.service.user;

import Ms_Login_and_Registers.dto.response.user.LoginResponse;
import Ms_Login_and_Registers.models.Permissions;
import Ms_Login_and_Registers.models.Roles;
import Ms_Login_and_Registers.repository.PermisionRepository;
import Ms_Login_and_Registers.repository.RolesRepository;
import Ms_Login_and_Registers.repository.UserRepository;
import Ms_Login_and_Registers.dto.request.user.LoginRequest;
import Ms_Login_and_Registers.models.User;
import Ms_Login_and_Registers.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService{
    private static final String BEARER_TYPE = "Bearer";

    @Autowired
    AuthenticationManager authenticationManager;
    @Value("${upload.path}")
    private String uploadPath; // Path to store uploaded files
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;


    @Autowired
    private PermisionRepository permissionRepository;
    @Autowired
    RolesRepository rolesRepository;
    @Override
    public LoginResponse process(LoginRequest request) {

        String username="";
        User user = userRepository.findByEmail(request.getEmail());

        if (user == null) {
            return new LoginResponse("User not found", false);
        }
        if (user!=null) {
            username = user.getUsername();
        }
    try {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailImpl userDetails = (UserDetailImpl) authentication.getPrincipal();
      //  Roles roles = rolesRepository.findById(userDetails.getUserrole()).get();

        return LoginResponse.builder()
                .token(jwt)
                .type(BEARER_TYPE)
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .name(userDetails.getName())
                .locked(userDetails.isLocked())
                .phone(userDetails.getPhone())
                .themeid(userDetails.getThemeid())
                .success(true).message("Login successful").id(userDetails.getId())
                .build();

    }catch (Exception e)
    {
       return new LoginResponse(e.getMessage().toString(), false);
    }


        }

    @Override
    public LoginResponse GetUserFromToken(String token) {
        boolean validetoken = false;

            validetoken = jwtUtils.validateJwtToken(token);

            if (validetoken) {
                String username = jwtUtils.getUserNameFromJwtToken(token);

                Optional<User> userfind = userRepository.findByUsername(username);

                return LoginResponse.builder()
                        .token(token)
                        .type(BEARER_TYPE)
                        .username(userfind.get().getUsername())
                        .email(userfind.get().getEmail())
                        .name(userfind.get().getName())
                        .locked(userfind.get().isLocked())
                        .phone(userfind.get().getPhone())
                        .themeid(userfind.get().getThemeid())
                        .images(userfind.get().getImages())
                        .id(userfind.get().getId())
                        .roles(userfind.get().getRoles())
                        .permissions(userfind.get().getPermissions())
                        .build();

            }
            return LoginResponse.builder().build();




    }

    @Override
    public User GetUserFromId(Long id) {

        return  userRepository.findById(id).orElseThrow();

    }
    public LoginResponse GetUserById(Long id) {
        Optional<User> userfind =userRepository.findById(id);
        return LoginResponse.builder()
                .username(userfind.get().getUsername())
                .email(userfind.get().getEmail())
                .name(userfind.get().getName())
                .locked(userfind.get().isLocked())
                .phone(userfind.get().getPhone())
                .themeid(userfind.get().getThemeid())
                .images(userfind.get().getImages())
                .id(userfind.get().getId())
                .roles(userfind.get().getRoles())
                .permissions(userfind.get().getPermissions())
                .build();
    }
    @Override
    public void SaveImageUser(MultipartFile file, Long id) throws Exception {
        try {
           User user = GetUserFromId(id);
            // Generate unique file name to avoid conflicts
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // Resolve the path to save the file
            Path path = Paths.get(uploadPath + File.separator + fileName);

           if (user!=null)
           {
               user.setImages(fileName);
               userRepository.save(user);
           }


            // Save the file to disk
            Files.copy(file.getInputStream(), path);
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage().toString());
        }
    }


    public void addRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Roles role = rolesRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        user.addRole(role);
        userRepository.save(user);
    }

    public void removeRoleFromUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Roles role = rolesRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        user.removeRole(role);
        userRepository.save(user);
    }

    public void addPermissionToUser(Long userId, Long permissionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Permissions permission = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("Permission not found"));
        user.addPermission(permission);
        userRepository.save(user);
    }

    public void removePermissionFromUser(Long userId, Long permissionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Permissions permission = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("Permission not found"));
        user.removePermission(permission);
        userRepository.save(user);
    }

    public User updateUser(Long userId, User updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update the user entity
        user.update(updatedUser);

        return userRepository.save(user);
    }
}
