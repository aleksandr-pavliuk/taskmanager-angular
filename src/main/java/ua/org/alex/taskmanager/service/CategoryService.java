package ua.org.alex.taskmanager.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.org.alex.taskmanager.entity.Category;
import ua.org.alex.taskmanager.repo.CategoryRepository;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@Service
@Transactional
public class CategoryService {

  private final CategoryRepository repository;

  public CategoryService(CategoryRepository repository) {
    this.repository = repository;
  }

  public List<Category> findAll(String email) {
    return repository.findByUserEmailOrderByTitleAsc(email);
  }

  public Category add(Category category) {
    return repository.save(category);
  }

  public Category update(Category category) {
    return repository.save(category);
  }

  public void deleteById(Long id) {
    repository.deleteById(id);
  }

  public List<Category> findByTitle(String title, String email){
    return repository.findByTitle(title, email);
  }

  public Category findById(Long id){
    return repository.findById(id).get();
  }
}
