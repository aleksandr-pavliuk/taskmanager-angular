package ua.org.alex.taskmanager.auth.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@Getter
@Setter
public class JsonException {

  private String exception;

  public JsonException(String exception) {
    this.exception = exception;
  }

}
