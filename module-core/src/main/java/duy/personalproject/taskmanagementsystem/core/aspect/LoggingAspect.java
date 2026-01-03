package duy.personalproject.taskmanagementsystem.core.aspect;

import duy.personalproject.taskmanagementsystem.core.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

/**
 * Aspect for logging execution time of methods annotated with @LogExecutionTime.
 * This aspect provides flexible, annotation-based execution time logging.
 * Supports both method-level and class-level annotations.
 */
@Aspect
@Component
@Slf4j(topic = "LOGGING_EXECUTION_TIME_ASPECT")
public class LoggingAspect {

    /**
     * Logs execution time for methods or classes annotated with @LogExecutionTime.
     *
     * @param joinPoint the join point
     * @param logExecutionTime the annotation (only present for method-level annotations)
     * @return the method result
     * @throws Throwable if method execution fails
     */
    @Around("@annotation(logExecutionTime) || @within(logExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, LogExecutionTime logExecutionTime) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();

            long executionTime = stopWatch.getTotalTimeMillis();

            LogExecutionTime annotation = getAnnotation(joinPoint, logExecutionTime);

            if (annotation != null) {
                logMethodExecution(className, methodName, executionTime, annotation);
            }

            return result;
        } catch (Throwable throwable) {
            stopWatch.stop();
            long executionTime = stopWatch.getTotalTimeMillis();
            log.error("Exception in [{}] {}() after {}ms: {}", className, methodName, executionTime, throwable.getMessage());
            throw throwable;
        }
    }

    /**
     * Gets the annotation from either the method or the class.
     */
    private LogExecutionTime getAnnotation(ProceedingJoinPoint joinPoint, LogExecutionTime providedAnnotation) {
        if (providedAnnotation != null) {
            return providedAnnotation;
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        LogExecutionTime methodAnnotation = method.getAnnotation(LogExecutionTime.class);
        if (methodAnnotation != null) {
            return methodAnnotation;
        }

        return joinPoint.getTarget().getClass().getAnnotation(LogExecutionTime.class);
    }

    /**
     * Logs the method execution with appropriate log level and details.
     */
    private void logMethodExecution(String className, String methodName, long executionTime, LogExecutionTime annotation) {
        String message = buildLogMessage(className, methodName, executionTime);

        switch (annotation.value()) {
            case DEBUG -> log.debug(message);
            case INFO -> log.info(message);
            case WARN -> log.warn(message);
        }
    }

    /**
     * Builds the log message based on annotation configuration.
     */
    private String buildLogMessage(String className, String methodName, long executionTime) {
        return "[%s] %s() executed in %d ms".formatted(className, methodName, executionTime);
    }
}

