package Ms_Login_and_Registers.service.user;

import Ms_Login_and_Registers.dto.response.user.LoginResponse;
import Ms_Login_and_Registers.dto.request.user.LoginRequest;

public interface LoginService {
    LoginResponse process(LoginRequest request);
    LoginResponse GetUserFromToken(String token);

}
