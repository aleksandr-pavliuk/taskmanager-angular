package ua.org.alex.taskmanager.controller;

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
import ua.org.alex.taskmanager.entity.Priority;
import ua.org.alex.taskmanager.search.PrioritySearchValues;
import ua.org.alex.taskmanager.service.PriorityService;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@RestController
@RequestMapping("/priority")
public class PriorityController {

  private PriorityService priorityService;

  public PriorityController(PriorityService priorityService) {
    this.priorityService = priorityService;
  }

  @PostMapping("/all")
  public List<Priority> findAll(@RequestBody String email) {
    return priorityService.findAll(email);
  }

  @PostMapping("/add")
  public ResponseEntity<Priority> add(@RequestBody Priority priority) {

    if (priority.getId() != null && priority.getId() != 0) {
      return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
    }

    if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
      return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
    }

    if (priority.getColor() == null || priority.getColor().trim().length() == 0) {
      return new ResponseEntity("missed param: color", HttpStatus.NOT_ACCEPTABLE);
    }

    return ResponseEntity.ok(priorityService.add(priority));
  }

  @PutMapping("/update")
  public ResponseEntity<Priority> update(@RequestBody Priority priority) {

    if (priority.getId() == null || priority.getId() == 0) {
      return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
    }

    if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
      return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
    }

    if (priority.getColor() == null || priority.getColor().trim().length() == 0) {
      return new ResponseEntity("missed param: color", HttpStatus.NOT_ACCEPTABLE);
    }

    priorityService.update(priority);

    return new ResponseEntity(HttpStatus.OK);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Priority> delete(@PathVariable("id") Long id) {

    try {
      priorityService.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      e.printStackTrace();
      return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
    }
    return new ResponseEntity(HttpStatus.OK);
  }

  @PostMapping("/search")
  public ResponseEntity<List<Priority>> search(
      @RequestBody PrioritySearchValues prioritySearchValues) {

    if (prioritySearchValues.getEmail() == null
        && prioritySearchValues.getEmail().trim().length() == 0) {
      return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
    }

    List<Priority> list = priorityService.findByTitle(prioritySearchValues.getTitle(),
        prioritySearchValues.getEmail());

    return ResponseEntity.ok(list);
  }

  @PostMapping("/id")
  public ResponseEntity<Priority> findById(@RequestBody Long id) {

    Priority priority = null;

    try {
      priority = priorityService.findById(id);
    } catch (NoSuchElementException e) { // если объект не будет найден
      e.printStackTrace();
      return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
    }

    return ResponseEntity.ok(priority);
  }
}
