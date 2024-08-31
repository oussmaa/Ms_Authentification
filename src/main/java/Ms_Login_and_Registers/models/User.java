package Ms_Login_and_Registers.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_pus")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @NotBlank
    @Size(max = 50)
    private String name;

    private boolean locked;

    @Size(max = 20)
    private String phone;

    private Long themeid;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonManagedReference
    private Set<Roles> roles;

    private String images;

    private String adress;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @JsonManagedReference
    private Set<Permissions> permissions;






    // Methods to manage permissions
    public void addPermission(Permissions permission) {
        this.permissions.add(permission);
        permission.getUsers().add(this);
    }

    public void removePermission(Permissions permission) {
        this.permissions.remove(permission);
        permission.getUsers().remove(this);
    }
    public void update(User newUser) {
        this.setUsername(newUser.getUsername());
        this.setEmail(newUser.getEmail());
        this.setPassword(newUser.getPassword());
        this.setName(newUser.getName());
        this.setLocked(newUser.isLocked());
        this.setPhone(newUser.getPhone());
        this.setThemeid(newUser.getThemeid());
        this.setImages(newUser.getImages());

     }
}
