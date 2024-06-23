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
@Table(name = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String descrption;
    private Date DefaultDate = new Date();

    @Column(unique = true)
    private String roles;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private Set<User> users;
}