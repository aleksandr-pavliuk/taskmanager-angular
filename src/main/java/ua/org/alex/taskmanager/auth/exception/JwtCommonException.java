package ua.org.alex.taskmanager.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
public class JwtCommonException extends AuthenticationException {

  public JwtCommonException(String msg) {
    super(msg);
  }

  public JwtCommonException(String msg, Throwable t) {
    super(msg, t);
  }
}