package ua.org.alex.taskmanager.business.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.org.alex.taskmanager.business.entity.Stat;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@Repository
public interface StatRepository extends CrudRepository<Stat, Long> {

  Stat findByUserEmail(String email);
}
