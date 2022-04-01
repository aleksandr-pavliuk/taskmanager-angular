package ua.org.alex.taskmanager.auth.repository;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.org.alex.taskmanager.auth.entity.Activity;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {

  @Modifying
  @Transactional
  @Query("UPDATE Activity a SET a.activated = :active WHERE a.uuid=:uuid")
  int changeActivated(@Param("uuid") String uuid, @Param("active") boolean active);

  Optional<Activity> findByUserId(Long id);

  Optional<Activity> findByUuid(String uuid);
}
