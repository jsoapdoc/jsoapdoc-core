package io.github.jsoapdoc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EndpointInfo {

    String name();

    String description() default "";

    String version() default "0.1";

    String[] rolesAllowed() default {};
}
