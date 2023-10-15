package me.staek.chapter05.item33.bounded_type_token;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * 한정적 타입 토큰
 */
public class PrintAnnotation {

    static Annotation getAnnotation(AnnotatedElement element, String annotationTypeName) {
        Class<?> annotationType = null; // 비한정적 타입 토큰
        try {
            annotationType = Class.forName(annotationTypeName);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
        Annotation annotation = element.getAnnotation(annotationType.asSubclass(Annotation.class));
        return element.getAnnotation(annotationType.asSubclass(Annotation.class));
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getAnnotation(MyService.class, MyAnnotation.class.getName()));
    }
}
