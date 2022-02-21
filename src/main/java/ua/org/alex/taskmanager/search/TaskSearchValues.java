package ua.org.alex.taskmanager.search;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskSearchValues {

  private String title;
  private Integer completed;
  private Long priorityId;
  private Long categoryId;
  private String email;
  private Date dateFrom;
  private Date dateTo;

  private Integer pageNumber;
  private Integer pageSize;

  private String sortColumn;
  private String sortDirection;

}
