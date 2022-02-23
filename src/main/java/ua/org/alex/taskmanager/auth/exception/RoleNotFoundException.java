package ua.org.alex.taskmanager.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
public class RoleNotFoundException extends AuthenticationException {

  public RoleNotFoundException(String msg) {
    super(msg);
  }

  public RoleNotFoundException(String msg, Throwable t) {
    super(msg, t);
  }
}
