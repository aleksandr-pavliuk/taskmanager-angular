package ua.org.alex.taskmanager.controller;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.org.alex.taskmanager.entity.Task;
import ua.org.alex.taskmanager.search.TaskSearchValues;
import ua.org.alex.taskmanager.service.TaskService;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@RestController
@RequestMapping("/task")
public class TaskController {

  public static final String ID_COLUMN = "id";
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PostMapping("/all")
  public ResponseEntity<List<Task>> findAll(@RequestBody String email) {
    return ResponseEntity.ok(taskService.findAll(email));
  }

  @PostMapping("/add")
  public ResponseEntity<Task> add(@RequestBody Task task) {

    if (task.getId() != null && task.getId() != 0) {
      return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
    }

    if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
      return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
    }

    return ResponseEntity.ok(taskService.add(task));

  }


  @PutMapping("/update")
  public ResponseEntity<Task> update(@RequestBody Task task) {

    if (task.getId() == null || task.getId() == 0) {
      return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
    }

    if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
      return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
    }

    taskService.update(task);

    return new ResponseEntity(HttpStatus.OK);

  }

  @PutMapping("/delete")
  public ResponseEntity delete(@RequestBody Long id) {

    try {
      taskService.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      e.printStackTrace();
      return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
    }
    return new ResponseEntity(HttpStatus.OK);
  }

  @PostMapping("/id")
  public ResponseEntity<Task> findById(@RequestBody Long id) {

    Task task = null;

    try {
      task = taskService.findById(id);
    } catch (NoSuchElementException e) {
      e.printStackTrace();
      return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
    }

    return ResponseEntity.ok(task);
  }

  @PostMapping("/search")
  public ResponseEntity<Page<Task>> search(@RequestBody TaskSearchValues taskSearchValues)
      throws ParseException {

    String title = taskSearchValues.getTitle() != null ? taskSearchValues.getTitle() : null;

    Boolean completed =
        taskSearchValues.getCompleted() != null && taskSearchValues.getCompleted() == 1;

    Long priorityId =
        taskSearchValues.getPriorityId() != null ? taskSearchValues.getPriorityId() : null;
    Long categoryId =
        taskSearchValues.getCategoryId() != null ? taskSearchValues.getCategoryId() : null;

    String sortColumn =
        taskSearchValues.getSortColumn() != null ? taskSearchValues.getSortColumn() : null;
    String sortDirection =
        taskSearchValues.getSortDirection() != null ? taskSearchValues.getSortDirection() : null;

    Integer pageNumber =
        taskSearchValues.getPageNumber() != null ? taskSearchValues.getPageNumber() : null;
    Integer pageSize =
        taskSearchValues.getPageSize() != null ? taskSearchValues.getPageSize() : null;

    String email = taskSearchValues.getEmail() != null ? taskSearchValues.getEmail()
        : null; // для показа задач только этого пользователя

    Date dateFrom = null;
    Date dateTo = null;

    if (taskSearchValues.getDateFrom() != null) {
      Calendar calendarFrom = Calendar.getInstance();
      calendarFrom.setTime(taskSearchValues.getDateFrom());
      calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
      calendarFrom.set(Calendar.MINUTE, 1);
      calendarFrom.set(Calendar.SECOND, 1);
      calendarFrom.set(Calendar.MILLISECOND, 1);

      dateFrom = calendarFrom.getTime();

    }

    if (taskSearchValues.getDateTo() != null) {

      Calendar calendarTo = Calendar.getInstance();
      calendarTo.setTime(taskSearchValues.getDateTo());
      calendarTo.set(Calendar.HOUR_OF_DAY, 23);
      calendarTo.set(Calendar.MINUTE, 59);
      calendarTo.set(Calendar.SECOND, 59);
      calendarTo.set(Calendar.MILLISECOND, 999);

      dateTo = calendarTo.getTime();

    }

    Sort.Direction direction =
        sortDirection == null || sortDirection.trim().length() == 0 || sortDirection.trim()
            .equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

    Sort sort = Sort.by(direction, sortColumn, ID_COLUMN);

    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

    Page<Task> result = taskService.findByParams(title, completed, priorityId, categoryId, email,
        dateFrom, dateTo, pageRequest);

    return ResponseEntity.ok(result);

  }

}
