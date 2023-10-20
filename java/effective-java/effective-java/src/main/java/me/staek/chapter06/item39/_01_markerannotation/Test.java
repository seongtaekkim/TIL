package me.staek.chapter06.item39._01_markerannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method is a test method.
 * Use only on parameterless static methods.
 */
@Retention(RetentionPolicy.RUNTIME) // runtime에 조회해야 함
@Target(ElementType.METHOD) // method element 에만 작성가능.
public @interface Test {
}
