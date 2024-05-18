package Ms_Login_and_Registers.repository;


import Ms_Login_and_Registers.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {

    Roles findByRoles(String roles);
/* hi */
}
