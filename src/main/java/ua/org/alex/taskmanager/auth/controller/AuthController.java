package ua.org.alex.taskmanager.auth.controller;

import static ua.org.alex.taskmanager.auth.service.UserService.DEFAULT_ROLE;

import java.util.UUID;
import javax.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.org.alex.taskmanager.auth.entity.Activity;
import ua.org.alex.taskmanager.auth.entity.Role;
import ua.org.alex.taskmanager.auth.entity.User;
import ua.org.alex.taskmanager.auth.exception.RoleNotFoundException;
import ua.org.alex.taskmanager.auth.exception.UserAlreadyActivatedException;
import ua.org.alex.taskmanager.auth.exception.UserOrEmailExistException;
import ua.org.alex.taskmanager.auth.objects.JsonException;
import ua.org.alex.taskmanager.auth.service.EmailService;
import ua.org.alex.taskmanager.auth.service.UserDetailsImpl;
import ua.org.alex.taskmanager.auth.service.UserDetailsServiceImpl;
import ua.org.alex.taskmanager.auth.service.UserService;
import ua.org.alex.taskmanager.auth.util.CookieUtils;
import ua.org.alex.taskmanager.auth.util.JwtUtils;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@RestController
@RequestMapping("/auth")
@Log
public class AuthController {

  private final UserService userService;
  private final EmailService emailService;
  private final PasswordEncoder encoder;
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;
  private final CookieUtils cookieUtils;
  private final UserDetailsServiceImpl userDetailsService;

  @Autowired
  public AuthController(UserService userService,
      PasswordEncoder encoder,
      AuthenticationManager authenticationManager, JwtUtils jwtUtils, CookieUtils cookieUtils,
      EmailService emailService, UserDetailsServiceImpl userDetailsService) {
    this.userService = userService;
    this.encoder = encoder;
    this.authenticationManager = authenticationManager;
    this.jwtUtils = jwtUtils;
    this.cookieUtils = cookieUtils;
    this.emailService = emailService;
    this.userDetailsService = userDetailsService;
  }

  @PostMapping("/testnoauth")
  public String testNoAuth() {
    return "OK-no-auth";
  }

  @PostMapping("/testwithauth")
  @PreAuthorize("hasAuthority('USER')")
  public String testWIthAuth() {
    return "OK-with-auth";
  }

  @PutMapping("/register")
  public ResponseEntity register(@Valid @RequestBody User user) {

    if (userService.userExist(user.getUsername(), user.getEmail())) {
      throw new UserOrEmailExistException("User or email already exists");
    }

    Role userRole = userService.findByName(DEFAULT_ROLE)
        .orElseThrow(() -> new RoleNotFoundException("Default Role USER not found."));
    user.getRoles().add(userRole);

    user.setPassword(encoder.encode(user.getPassword()));

    Activity activity = new Activity();
    activity.setActivated(false);
    activity.setUser(user);
    activity.setUuid(UUID.randomUUID().toString());

    userService.register(user, activity);

    emailService.sendActivationEmail(user.getEmail(), user.getUsername(), activity.getUuid());

    return ResponseEntity.ok().build();
  }

  @PostMapping("/activate-account")
  public ResponseEntity<Boolean> activateUser(@RequestBody String uuid) {

    Activity activity = userService.findActivityByUuid(uuid)
        .orElseThrow(() -> new UsernameNotFoundException("Activity Not Found with uuid: " + uuid));

    if (activity.getActivated()) {
      throw new UserAlreadyActivatedException("User already activated");
    }

    int updateCount = userService.activate(uuid);

    return ResponseEntity.ok(updateCount == 1);
  }

  @PostMapping("/login")
  public ResponseEntity<User> login(@Valid @RequestBody User user) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    if (!userDetails.isActivated()) {
      throw new DisabledException("User disabled");
    }

    String jwt = jwtUtils.createAccessToken(userDetails.getUser());

    userDetails.getUser().setPassword(null);

    HttpCookie cookie = cookieUtils.createJwtCookie(jwt);

    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());

    return ResponseEntity.ok().headers(responseHeaders).body(userDetails.getUser());
  }

  @PostMapping("/logout")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity logout() {

    HttpCookie cookie = cookieUtils.deleteCookie();
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());

    return ResponseEntity.ok().headers(responseHeaders).build();
  }

  @PostMapping("update-password")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<Boolean> updatePassword(@RequestBody String password) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

    int updateCount = userService.updatePassword(encoder.encode(password), user.getUsername());

    return ResponseEntity.ok(updateCount == 1);
  }

  @PostMapping("/resend-activate-email")
  public ResponseEntity resendActivateEmail(@RequestBody String usernameOrEmail) {

    UserDetailsImpl user = (UserDetailsImpl) userDetailsService.loadUserByUsername(usernameOrEmail);

    Activity activity = userService.findActivityByUserId(user.getId())
        .orElseThrow(() -> new UsernameNotFoundException("Activity Not Found with user: " + usernameOrEmail));

    if (activity.getActivated())
      throw new UserAlreadyActivatedException("User already activated: " + usernameOrEmail);


    emailService.sendActivationEmail(user.getEmail(), user.getUsername(), activity.getUuid());

    return ResponseEntity.ok().build();
  }

  @PostMapping("/send-reset-password-email")
  public ResponseEntity sendEmailResetPassword(@RequestBody String email) {

    UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(email);

    User user = userDetails.getUser();

    if (userDetails != null) {
      emailService.sendResetPasswordEmail(user.getEmail(), jwtUtils.createEmailResetToken(user));
    }

    return ResponseEntity.ok().build();
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<JsonException> handleExceptions(Exception ex) {

    return new ResponseEntity(new JsonException(ex.getClass().getSimpleName()),
        HttpStatus.BAD_REQUEST);

  }
}
