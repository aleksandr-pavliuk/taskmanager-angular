package ua.org.alex.taskmanager.business.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import ua.org.alex.taskmanager.business.entity.Priority;
import ua.org.alex.taskmanager.business.repo.PriorityRepository;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@Service
@Transactional
public class PriorityService {

  private final PriorityRepository repository;

  public PriorityService(PriorityRepository repository) {
    this.repository = repository;
  }

  public List<Priority> findAll(String email) {
    return repository.findByUserEmailOrderByIdAsc(email);
  }

  public Priority add(Priority priority) {
    return repository.save(priority);
  }

  public Priority update(Priority priority) {
    return repository.save(priority);
  }

  public void deleteById(Long id) {
    repository.deleteById(id);
  }

  public List<Priority> findByTitle(String title, String email) {
    return repository.findByTitle(title, email);
  }

  public Priority findById(Long id) {
    return repository.findById(id).get();
  }
}
