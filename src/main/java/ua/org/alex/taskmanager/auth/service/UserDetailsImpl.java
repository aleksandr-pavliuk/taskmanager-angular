package ua.org.alex.taskmanager.auth.service;

import java.util.Collection;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.org.alex.taskmanager.auth.entity.User;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@Getter
@Setter
public class UserDetailsImpl implements UserDetails {

  private User user;
  private Collection<? extends GrantedAuthority> authorities;

  public UserDetailsImpl(User user) {
    this.user = user;

    authorities = user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
  }

  public long getId() {
    return user.getId();
  }

  public String getEmail() {
    return user.getEmail();
  }

  public boolean isActivated() {
    return user.activity.getActivated();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }



  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
