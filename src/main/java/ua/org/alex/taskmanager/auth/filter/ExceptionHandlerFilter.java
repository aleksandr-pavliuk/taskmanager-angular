package ua.org.alex.taskmanager.auth.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.org.alex.taskmanager.auth.objects.JsonException;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@Component
public class ExceptionHandlerFilter  extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    try {
      filterChain.doFilter(request, response);
    }catch (RuntimeException e){

      JsonException ex = new JsonException(e.getClass().getSimpleName());

      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().write(convertObjectToJson(ex));
    }

  }

  public String convertObjectToJson(Object object) throws JsonProcessingException {
    if (object == null){
      return null;
    }
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(object);
  }
}
