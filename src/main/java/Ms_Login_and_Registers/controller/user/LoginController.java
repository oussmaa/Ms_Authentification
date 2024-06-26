package Ms_Login_and_Registers.controller.user;

import Ms_Login_and_Registers.dto.response.user.LoginResponse;
import Ms_Login_and_Registers.dto.request.user.LoginRequest;
import Ms_Login_and_Registers.models.User;
import Ms_Login_and_Registers.repository.UserRepository;
import Ms_Login_and_Registers.service.user.LoginService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath; // Path to store uploaded files

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("id") long id) throws Exception {
        if (file.isEmpty()) {
            return "Please select a file to upload";
        }
        try {
            loginService.SaveImageUser(file, id);

            return "File uploaded successfully";
        } catch (Exception e) {
            throw new Exception(e.getMessage().toString());
        }

    }

    @PostMapping("/loginuser")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = loginService.process(loginRequest);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        try {
            File file = new File(uploadPath + '/' + filename);
            byte[] image = FileUtils.readFileToByteArray(file);

            // Detect content type dynamically
            String contentType = Files.probeContentType(Paths.get(file.getAbsolutePath()));

            // If content type is not found, default to image/jpeg
            if (contentType == null) {
                contentType = MediaType.IMAGE_JPEG_VALUE;
            }

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(image);
        } catch (FileNotFoundException e) {
            // Handle file not found exception
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new byte[0]);
        } catch (IOException e) {
            // Handle other IO exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[0]);
        }
    }
    @GetMapping("/GetAllUsers")
    public ResponseEntity<List<User>> GetAllUsers()
    {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/GetUser")
    public ResponseEntity<LoginResponse> GetUserFromToken(@RequestBody LoginResponse loginResponse) {
        try {
            System.out.println(loginResponse.getToken());
            LoginResponse response = loginService.GetUserFromToken(loginResponse.getToken());
            if (response.getToken() == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoginResponse("Token Is Invalid",false));
            }
            else {
                return ResponseEntity.status(HttpStatus.OK).body(response);

            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(e.getMessage().toString(),false));

        }

    }
    @GetMapping("/{id}")
    public  User getUserById(@PathVariable Long id) {
        return loginService.GetUserFromId(id);


    }

}
