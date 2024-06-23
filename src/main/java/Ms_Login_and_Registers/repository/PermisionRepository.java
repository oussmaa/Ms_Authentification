package Ms_Login_and_Registers.repository;


import Ms_Login_and_Registers.models.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermisionRepository extends JpaRepository<Permissions,Long> {

   Permissions findByCode(String code);

}
