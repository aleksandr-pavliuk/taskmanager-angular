package ua.org.alex.taskmanager.auth.repository;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.org.alex.taskmanager.auth.entity.User;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("select case when count(u)>0 then true else false end from User u where lower(u.email) = lower(:email)")
  boolean existByEmail(@Param("email") String email);

  @Query("select case when count(u)>0 then true else false end from User u where lower(u.username) = lower(:username)")
  boolean existByUsername(@Param("username") String username);

  Optional<User> findByUsername(String username);

  Optional<User> findUserByEmail(String email);

  @Modifying
  @Transactional
  @Query("UPDATE User u SET u.password = :password WHERE u.username = :username")
  int updatePassword(@Param("password") String password, @Param("username") String usermane);

}
