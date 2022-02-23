package ua.org.alex.taskmanager.business.controller;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.org.alex.taskmanager.business.entity.Category;
import ua.org.alex.taskmanager.business.search.CategorySearchValues;
import ua.org.alex.taskmanager.business.service.CategoryService;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

  private CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @PostMapping("/all")
  public List<Category> findAll(@RequestBody String email) {
    return categoryService.findAll(email);
  }

  @PostMapping("/add")
  public ResponseEntity<Category> add(@RequestBody Category category) {

    if (category.getId() != null && category.getId() != 0) {
      return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
    }

    if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
      return new ResponseEntity("missed param: title MUST be not null", HttpStatus.NOT_ACCEPTABLE);
    }

    return ResponseEntity.ok(categoryService.add(category));
  }

  @PutMapping("/update")
  public ResponseEntity<Category> update(@RequestBody Category category) {

    if (category.getId() == null && category.getId() == 0) {
      return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
    }

    if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
      return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
    }

    categoryService.update(category);

    return new ResponseEntity(HttpStatus.OK);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Category> delete(@PathVariable("id") Long id) {

    try {
      categoryService.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      e.printStackTrace();
      return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
    }
    return new ResponseEntity(HttpStatus.OK);
  }

  @PostMapping("/search")
  public ResponseEntity<List<Category>> search(
      @RequestBody CategorySearchValues categorySearchValues) {

    if (categorySearchValues.getEmail() == null
        && categorySearchValues.getEmail().trim().length() == 0) {
      return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
    }

    List<Category> list = categoryService.findByTitle(categorySearchValues.getTitle(),
        categorySearchValues.getEmail());

    return ResponseEntity.ok(list);
  }

  @PostMapping("/id")
  public ResponseEntity<Category> findById(@RequestBody Long id) {

    Category category = null;

    try {
      category = categoryService.findById(id);
    } catch (NoSuchElementException e) {
      e.printStackTrace();
      return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
    }

    return ResponseEntity.ok(category);
  }

}
