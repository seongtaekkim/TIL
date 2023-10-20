package me.staek.chapter06.item39._04_repeatableannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ExceptionTest 를 반복사용하기 위한 Container annotation 정의.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTestContainer {
    ExceptionTest[] value(); // @ExceptionTest는 배열형태로 저장된다.
}
