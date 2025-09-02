package co.edu.escuelaing.webframe.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark method parameters as request parameters.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {
    /**
     * The name of the request parameter.
     */
    String value();
    
    /**
     * Default value if parameter is not present.
     */
    String defaultValue() default "";
}
