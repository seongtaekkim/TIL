package me.staek;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // Interface, Class, Enum에 적용됨
@Retention(RetentionPolicy.SOURCE)
public @interface  ProcessorInterface {}
