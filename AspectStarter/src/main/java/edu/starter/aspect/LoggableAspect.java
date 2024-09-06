package edu.starter.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggableAspect {
    @Pointcut("within(@edu.starter.annotations.Loggable *) && execution(* * (..))")
    public void annotatedByLoggable() {
    }

    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Calling method: " + joinPoint.getSignature().getName());
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long execTime = System.currentTimeMillis() - startTime;
        System.out.println("Execution of method: " + joinPoint.getSignature() + " finished in " + execTime + " ms");
        return result;
    }
}
