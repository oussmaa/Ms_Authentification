package Ms_Login_and_Registers.service.user;

import Ms_Login_and_Registers.dto.request.user.RegisterRequest;
import Ms_Login_and_Registers.exception.InvalidRequestException;

public interface RegisterService {

    void process(RegisterRequest request) throws InvalidRequestException;
}
