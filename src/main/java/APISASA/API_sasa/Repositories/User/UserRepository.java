package APISASA.API_sasa.Repositories.User;

import jakarta.persistence.Entity;

@Entity
public interface UserRepository extends JpaRepository<UserEntity, Long>{
}
