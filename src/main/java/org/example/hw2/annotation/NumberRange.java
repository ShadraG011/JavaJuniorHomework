package org.example.hw2.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Для задания 4. Аннотация для создания массива чисел для дальнейшего их использования в методе
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NumberRange {
    int[] numberRange() default {};
}
