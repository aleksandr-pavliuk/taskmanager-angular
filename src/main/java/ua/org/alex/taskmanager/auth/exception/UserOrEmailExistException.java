package ua.org.alex.taskmanager.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
public class UserOrEmailExistException extends AuthenticationException {

  public UserOrEmailExistException(String msg) {
    super(msg);
  }

  public UserOrEmailExistException(String msg, Throwable t) {
    super(msg, t);
  }

}
