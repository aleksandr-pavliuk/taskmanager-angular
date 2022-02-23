package ua.org.alex.taskmanager.auth.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.org.alex.taskmanager.auth.entity.Activity;
import ua.org.alex.taskmanager.auth.entity.Role;
import ua.org.alex.taskmanager.auth.entity.User;
import ua.org.alex.taskmanager.auth.repository.ActivityRepository;
import ua.org.alex.taskmanager.auth.repository.RoleRepository;
import ua.org.alex.taskmanager.auth.repository.UserRepository;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@Service
public class UserService {

  public static final String DEFAULT_ROLE = "USER";

  private UserRepository userRepository;
  private RoleRepository roleRepository;
  private ActivityRepository activityRepository;

  @Autowired
  public UserService(UserRepository userRepository, RoleRepository roleRepository,
      ActivityRepository activityRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.activityRepository = activityRepository;
  }

  public User getUser(Long id) {
    return userRepository.findById(id).get();
  }

  public void register(User user, Activity activity) {
    activityRepository.save(activity);
    userRepository.save(user);

  }

  public boolean userExist(String username, String email) {

    if (userRepository.existByUsername(username)) {
      return true;
    }

    if (userRepository.existByEmail(email)) {
      return true;
    }
    return false;
  }

  public Optional<Role> findByName(String role) {
    return roleRepository.findByName(role);
  }

  public Activity saveActivity(Activity activity){
    return activityRepository.save(activity);
  }

  public Optional<Activity> findActivityByUserId(long id){
    return activityRepository.findByUserId(id);
  }

  public Optional<Activity> findActivityByUuid(String uuid) {
    return activityRepository.findByUuid(uuid);
  }

  public int activate(String uuid) {
    return activityRepository.changeActivated(uuid, true);
  }

  public int deactivate(String uuid) {
    return activityRepository.changeActivated(uuid, false);
  }

  public int updatePassword(String password, String username){
    return userRepository.updatePassword(password, username);
  }

}
