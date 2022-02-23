package ua.org.alex.taskmanager.business.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.org.alex.taskmanager.business.entity.Priority;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {


  List<Priority> findByUserEmailOrderByIdAsc(String email);

  @Query("SELECT c FROM Priority c where "
      + "(:title is null or :title='' "
      + "or lower(c.title) like lower(concat('%',:title,'%'))) "
      + "and c.user.email=:email "
      + "order by c.title asc")
  List<Priority> findByTitle(@Param("title") String title, @Param("email") String email);

}
