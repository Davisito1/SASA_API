package APISASA.API_sasa.Repositories.Usuario;

import APISASA.API_sasa.Entities.Usuario.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
