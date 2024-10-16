package com.jsoapdoc.soaplib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodDetails {
    String description() default "";

    Class<?> requestType();

    Class<?> responseType();

    String version() default "1.0";

    String[] rolesAllowed() default {};

    boolean printInfo() default false;
}