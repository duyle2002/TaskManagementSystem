package duy.personalproject.taskmanagementsystem.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to enable execution time logging for methods or classes.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {

    LogLevel value() default LogLevel.INFO;

    enum LogLevel {
        DEBUG, INFO, WARN
    }
}
