package ua.org.alex.taskmanager.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.org.alex.taskmanager.entity.Category;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  List<Category> findByUserEmailOrderByTitleAsc(String email);

  @Query("SELECT c FROM Category c where "
      + "(:title is null or :title='' "
      + "or lower(c.title) like lower(concat('%',:title,'%'))) "
      + "and c.user.email=:email "
      + "order by c.title asc")
  List<Category> findByTitle(@Param("title") String title, @Param("email") String email);
}
