package ua.org.alex.taskmanager.service;

import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ua.org.alex.taskmanager.entity.Task;
import ua.org.alex.taskmanager.repo.TaskRepository;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@Service
@Transactional
public class TaskService {

  private final TaskRepository repository; // сервис имеет право обращаться к репозиторию (БД)

  public TaskService(TaskRepository repository) {
    this.repository = repository;
  }


  public List<Task> findAll(String email) {
    return repository.findByUserEmailOrderByTitleAsc(email);
  }

  public Task add(Task task) {
    return repository.save(task);
  }

  public Task update(Task task) {
    return repository.save(task);
  }

  public void deleteById(Long id) {
    repository.deleteById(id);
  }


  public Page<Task> findByParams(String text, Boolean completed, Long priorityId, Long categoryId, String email, Date dateFrom, Date dateTo, PageRequest paging) {
    return repository.findByParams(text, completed, priorityId, categoryId, email, dateFrom, dateTo, paging);
  }

  public Task findById(Long id) {
    return repository.findById(id).get();
  }

}
