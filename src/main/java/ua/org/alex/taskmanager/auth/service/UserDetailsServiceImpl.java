package ua.org.alex.taskmanager.auth.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.org.alex.taskmanager.auth.entity.User;
import ua.org.alex.taskmanager.auth.repository.UserRepository;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

    Optional<User> userOptional = userRepository.findByUsername(username);

    if (!userOptional.isPresent()) {
      userOptional = userRepository.findUserByEmail(username);
    }

    if (!userOptional.isPresent()) {
      throw new UsernameNotFoundException("User Not Found with username or email: " + username);
    }

    return new UserDetailsImpl(userOptional.get());
  }

}
