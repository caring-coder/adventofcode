package pro.verron.aoc.core;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
public @interface Delimiter {
    String value() default "\n";
}
