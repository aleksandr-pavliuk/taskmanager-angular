package ua.org.alex.taskmanager.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.SessionManagementFilter;
import ua.org.alex.taskmanager.auth.filter.AuthTokenFilter;
import ua.org.alex.taskmanager.auth.filter.ExceptionHandlerFilter;
import ua.org.alex.taskmanager.auth.service.UserDetailsServiceImpl;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableAsync
public class SpringConfig extends WebSecurityConfigurerAdapter {
  
  private UserDetailsServiceImpl userDetailsService;

  private AuthTokenFilter authTokenFilter;
  private ExceptionHandlerFilter exceptionHandlerFilter;

  @Autowired
  public void setUserDetailsService(
      UserDetailsServiceImpl userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Autowired
  public void setAuthTokenFilter(AuthTokenFilter authTokenFilter){
    this.authTokenFilter = authTokenFilter;
  }

  @Autowired
  public void setExceptionHandlerFilter(ExceptionHandlerFilter exceptionHandlerFilter){
    this.exceptionHandlerFilter = exceptionHandlerFilter;
  }


  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
    authenticationManagerBuilder
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());
  }

  @Bean
  public FilterRegistrationBean registration(AuthTokenFilter filter){
    FilterRegistrationBean registration = new FilterRegistrationBean(filter);
    registration.setEnabled(false);
    return registration;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable(); // на время разработки проекта не будет ошибок (для POST, PUT и др. запросов) - недоступен и т.д.
    http.formLogin()
        .disable(); // отключаем, т.к. форма авторизации создается не на Spring технологии (например, Spring MVC + JSP), а на любой другой клиентской технологии
    http.httpBasic().disable(); // отключаем стандартную браузерную форму авторизации
//    http.requiresChannel().anyRequest().requiresSecure(); // обязательное исп. HTTPS для всех запросах
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.addFilterBefore(authTokenFilter, SessionManagementFilter.class);
    http.addFilterBefore(exceptionHandlerFilter, AuthTokenFilter.class);
  }

}
