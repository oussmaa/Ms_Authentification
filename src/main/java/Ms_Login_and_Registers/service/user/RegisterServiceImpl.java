package Ms_Login_and_Registers.service.user;

import Ms_Login_and_Registers.repository.UserRepository;
import Ms_Login_and_Registers.dto.request.user.RegisterRequest;
import Ms_Login_and_Registers.exception.InvalidRequestException;
import Ms_Login_and_Registers.models.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    UserRepository userRepository;



    PasswordEncoder encoder;

    @Override
    public void process(RegisterRequest request) throws InvalidRequestException {
        boolean usernameExists = userRepository.existsByUsername(request.getUsername());
        if (usernameExists) throw new InvalidRequestException("username already exists");

        boolean emailExists = userRepository.existsByEmail(request.getEmail());
        if (emailExists) throw new InvalidRequestException("email already exists");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setThemeid(request.getThemeid());
        user.setUserrole(request.getUserrole());
        user.setLocked(request.isLocked());
        user.setPhone(request.getPhone());
        user.setPassword(encoder.encode(request.getPassword()));
        userRepository.save(user);

    }
}
