package Ms_Login_and_Registers.dto.response.user;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String type;
    private String username;
    private String email;
    private String name;
    private boolean locked;
    private String phone;
    private Long themeid;
    private Long userrole;
}


