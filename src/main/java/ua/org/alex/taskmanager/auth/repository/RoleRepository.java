package ua.org.alex.taskmanager.auth.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.org.alex.taskmanager.auth.entity.Role;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByName(String name);

  Optional<Role> findById(Long id);
}
