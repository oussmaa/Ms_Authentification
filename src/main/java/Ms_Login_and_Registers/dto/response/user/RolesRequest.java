package Ms_Login_and_Registers.dto.response.user;

 import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class RolesRequest {

    private String roles;
    private String descrption;
    private Set<String> permissions ;

}
