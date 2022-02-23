package ua.org.alex.taskmanager.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
public class UserAlreadyActivatedException extends AuthenticationException {

  public UserAlreadyActivatedException(String msg) {
    super(msg);
  }

  public UserAlreadyActivatedException(String msg, Throwable t) {
    super(msg, t);
  }
}
