package Ms_Login_and_Registers.service.user;

import Ms_Login_and_Registers.dto.response.user.LoginResponse;
import Ms_Login_and_Registers.dto.request.user.LoginRequest;
import Ms_Login_and_Registers.models.User;
import org.springframework.web.multipart.MultipartFile;

public interface LoginService {
    LoginResponse process(LoginRequest request);
    LoginResponse GetUserFromToken(String token);
    User GetUserFromId(Long id);

    void SaveImageUser(MultipartFile file,Long id) throws Exception;

}
