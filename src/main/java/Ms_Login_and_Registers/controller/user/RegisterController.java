package Ms_Login_and_Registers.controller.user;

import Ms_Login_and_Registers.dto.request.user.RegisterRequest;
import Ms_Login_and_Registers.dto.response.BasicResponse;
import Ms_Login_and_Registers.exception.InvalidRequestException;
import Ms_Login_and_Registers.service.user.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @PostMapping("/adduser")
    public ResponseEntity<BasicResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {

        try {
            registerService.process(registerRequest);
            return ResponseEntity.ok(BasicResponse.builder()
                    .message("Success")
                    .build());

        } catch (InvalidRequestException e) {
            return ResponseEntity.badRequest().body(BasicResponse.builder()
                    .message(e.getMessage())
                    .build());
        }

    }

}
