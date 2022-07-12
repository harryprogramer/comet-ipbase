package io.lagpixel.comet.common;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {
    String reason() default "";
}
