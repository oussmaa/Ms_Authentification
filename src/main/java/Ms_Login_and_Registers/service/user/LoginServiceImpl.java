package Ms_Login_and_Registers.service.user;

import Ms_Login_and_Registers.dto.response.user.LoginResponse;
import Ms_Login_and_Registers.repository.UserRepository;
import Ms_Login_and_Registers.dto.request.user.LoginRequest;
import Ms_Login_and_Registers.models.User;
import Ms_Login_and_Registers.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService{
    private static final String BEARER_TYPE = "Bearer";

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;
    @Override
    public LoginResponse process(LoginRequest request) {

        String username="";
        User user = userRepository.findByEmail(request.getEmail());

        if (user!=null) {
            username = user.getUsername();
        }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailImpl userDetails = (UserDetailImpl) authentication.getPrincipal();


            return LoginResponse.builder()
                    .token(jwt)
                    .type(BEARER_TYPE)
                    .username(userDetails.getUsername())
                    .email(userDetails.getEmail())
                    .name(userDetails.getName())
                    .locked(userDetails.isLocked())
                    .phone(userDetails.getPhone())
                    .themeid(userDetails.getThemeid())
                    .userrole(userDetails.getUserrole())
                    .build();

        }


}
