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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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
    PasswordEncoder encoder;

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
    public String getFirstRoleName(Set<Roles> roles) {
        if (roles != null && !roles.isEmpty()) {
            // Get the first role using an iterator
            Roles firstRole = roles.iterator().next();
            return firstRole.getRoles(); // Assuming getName() is defined in Role
        }
        return null; // or handle the case where the set is empty
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
                        .roles(getFirstRoleName(userfind.get().getRoles()))
                        .permissions(userfind.get().getPermissions())
                        .adress(userfind.get().getAdress())
                        .password(userfind.get().getPassword())
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
                .adress(userfind.get().getAdress())
                .roles(getFirstRoleName(userfind.get().getRoles()))
                .permissions(userfind.get().getPermissions())
                .password(userfind.get().getPassword())
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




    public void removePermissionFromUser(Long userId, Long permissionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Permissions permission = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("Permission not found"));
        user.removePermission(permission);
        userRepository.save(user);
    }

    public User updateUser(Long userId, LoginResponse updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update basic user information
        if(updatedUser.getUsername()!="")
        {
            user.setUsername(updatedUser.getUsername());

        }
        if(updatedUser.getEmail()!="")
        {
            user.setEmail(updatedUser.getEmail());

        }
        if(updatedUser.getName()!="")
        {
            user.setName(updatedUser.getName());

        }
        Boolean isLocked = updatedUser.isLocked();

        if(isLocked != null) {
            user.setLocked(isLocked);
        }
        if(updatedUser.getPhone()!="")
        {
            user.setPhone(updatedUser.getPhone());

        }
        if(updatedUser.getThemeid()!=null)
        {
            user.setThemeid(updatedUser.getThemeid());

        }
        if(updatedUser.getAdress()!="")
        {
            user.setAdress(updatedUser.getAdress());

        }

        // Update password if provided (and encode it)
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            String encodedPassword = encoder.encode(updatedUser.getPassword());
            user.setPassword(encodedPassword);
        }

        // Update roles
        if(updatedUser.getRoles()!= null) {
            Set<Roles> roles = new HashSet<>();

            Roles role = rolesRepository.findByRoles(updatedUser.getRoles());
            if (role != null) {
                roles.add(role);
            }


            // Remove old roles not present in updated roles
            user.setRoles(roles);
        }
        // Update permissions
        if(updatedUser.getPermissions()!= null) {
            Set<Permissions> newPermissions = new HashSet<>();
            for (Permissions permissionName : updatedUser.getPermissions()) {
                Permissions permission = permissionRepository.findByCode(permissionName.getCode());
                if (permission != null) {
                    newPermissions.add(permission);
                }
            }

            // Remove old permissions not present in updated permissions
            user.getPermissions().removeIf(permission -> !newPermissions.contains(permission));

            // Add new permissions
            user.getPermissions().addAll(newPermissions);
        }
        return userRepository.save(user);
    }
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Optionally, remove associated roles

            user.getPermissions().clear(); // Clears all permissions associated with the user


        userRepository.delete(user);
    }
}
