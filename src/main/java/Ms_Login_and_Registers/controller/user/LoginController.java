package Ms_Login_and_Registers.controller.user;

import Ms_Login_and_Registers.dto.response.user.LoginResponse;
import Ms_Login_and_Registers.dto.request.user.LoginRequest;
import Ms_Login_and_Registers.service.user.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = loginService.process(loginRequest);

        return ResponseEntity.ok(response);
    }
}
