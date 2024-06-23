package Ms_Login_and_Registers.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "permissions")
public class Permissions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date DefaultDate = new Date();

    @Column(unique = true)
    private String code;

    @ManyToMany(mappedBy = "permissions")
    @JsonBackReference
    private Set<User> users;
}
