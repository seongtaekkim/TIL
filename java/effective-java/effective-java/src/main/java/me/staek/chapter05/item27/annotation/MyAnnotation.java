package me.staek.chapter05.item27.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * RetentionPolicy
 * - SOURCE: 컴파일단계까지 존재함.
 * - CLASS: 컴파일 이후 바이트코드 형태로 존재함
 * - RUNTIME: 프로그램 실행 후 메모리에 존재함
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface MyAnnotation {

}
