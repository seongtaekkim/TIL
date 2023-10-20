package me.staek.chapter06.item39._04_repeatableannotation;

import java.lang.annotation.*;

/**
 * Indicates that the annotated method is a test method that
 * must throw the designated exception to succeed.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ExceptionTestContainer.class) // @ExceptionTest 을 반복하여 사용할 수 있도록 annotation container 작성
public @interface ExceptionTest {
    Class<? extends Throwable> value();
}
