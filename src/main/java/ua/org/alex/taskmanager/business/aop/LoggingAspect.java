package ua.org.alex.taskmanager.business.aop;

import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@Aspect
@Component
@Log
public class LoggingAspect {

  @Around("execution(* ua.org.alex.taskmanager.controller..*(..)))")
  public Object profileControllerMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

    MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

    String className = methodSignature.getDeclaringType().getSimpleName();
    String methodName = methodSignature.getName();

    log.info("--------- Executing " + className + "." + methodName + "  ---------");

    final StopWatch stopWatch = new StopWatch();

    stopWatch.start();
    Object result = proceedingJoinPoint.proceed();
    stopWatch.stop();

    log.info("--------- Executing time of " + className + "." + methodName + " :: "
        + stopWatch.getTotalTimeMillis());

    return result;
  }

}
