package pro.verron.aoc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AdventOfCodeDelimiter {
    String value() default "\n";
}
