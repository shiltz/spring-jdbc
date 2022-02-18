package za.co.shilton.springjdbc.aspects;

import java.time.Duration;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class PerformanceLoggingAspect {

  /**
   * A custom pointcut for the GetMapping for Order
   */
  @Pointcut("execution(* za.co.shilton.springjdbc.controller.OrderController.getOrder(..))")
  public void getOrderJoinPoint() {

  }

  /**
   * A custom pointcut for all controllers
   */
  @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
  public void allRestControllers() {

  }

  @Pointcut("@annotation(za.co.shilton.springjdbc.annotations.Performance) && args(id)")
  public void performancePointCUt(UUID id) {

  }
  /**
   * Advice which links the action and the pointcut getOrderJoinPoint()
   */
//  @Around("getOrderJoinPoint()")
  @Around("performancePointCUt(orderID)")
  public Object log(ProceedingJoinPoint proceedingJoinPoint, UUID orderID) throws Throwable {
    log.info("Intercepted:" + proceedingJoinPoint.getSignature().toLongString());
    log.info("args length:" + proceedingJoinPoint.getArgs().length);
    log.info("args:" + orderID);
    var startTime = System.currentTimeMillis();
    var results = proceedingJoinPoint.proceed();
    var endTime = System.currentTimeMillis();
    var duration = Duration.ofMillis(endTime - startTime).toMillis();
    log.info("results:" + results);
    log.info("time taken (ms):" + duration);
    return results;
  }

  /**
   * Advice which links the action and the pointcut getOrderJoinPoint()
   */
//  @Around("getOrderJoinPoint()")
  public Object log2(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    log.info("2Intercepted:" + proceedingJoinPoint.getSignature().toLongString());
    log.info("2args length:" + proceedingJoinPoint.getArgs().length);
    log.info("2args:" + proceedingJoinPoint.getArgs()[0]);
    var results = proceedingJoinPoint.proceed();
    log.info("2results:" + results);
    return results;
  }

  /**
   * Advice which links the action and the pointcut allRestControllers()
   */
//  @Around("allRestControllers()")
  public Object log3(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    log.info("3Intercepted:" + proceedingJoinPoint.getSignature().toLongString());
    log.info("3args length:" + proceedingJoinPoint.getArgs().length);
    log.info("3args:" + proceedingJoinPoint.getArgs()[0]);
    var results = proceedingJoinPoint.proceed();
    log.info("3results:" + results);
    return results;
  }

}
