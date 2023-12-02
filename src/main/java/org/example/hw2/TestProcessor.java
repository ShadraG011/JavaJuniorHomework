package org.example.hw2;

import org.example.hw2.annotation.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TestProcessor {

    /**
     * Данный метод находит все void методы без аргументов (и с аргументами) в классе, и запускает их.
     * <p>
     * Для запуска создается тестовый объект с помощью конструктора без аргументов.
     */
    public static void runTest(Class<?> testClass) {
        final Constructor<?> declaredConstructor;
        try {
            declaredConstructor = testClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Для класса \"" + testClass.getName() + "\" не найден конструктор без аргументов");
        }

        final Object testObj;
        try {
            testObj = declaredConstructor.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось создать объект класса \"" + testClass.getName() + "\"");
        }

        List<Method> methods = new ArrayList<>(); // список для методов с аннотацией @Test
        List<Method> beforeMethods = new ArrayList<>(); // список для методов с аннотацией @BeforeEach
        List<Method> afterMethods = new ArrayList<>(); // список для методов с аннотацией @AfterEach

        for (Method method : testClass.getDeclaredMethods()) {
            if ((method.isAnnotationPresent(Test.class) && !method.isAnnotationPresent(NumberRange.class)) && !method.isAnnotationPresent(Skip.class)) {
                checkTestMethod(method);
                methods.add(method);
            } else if (method.isAnnotationPresent(BeforeEach.class)) {
                checkTestMethod(method);
                beforeMethods.add(method);
            } else if (method.isAnnotationPresent(AfterEach.class)) {
                checkTestMethod(method);
                afterMethods.add(method);
            } else if (method.isAnnotationPresent(NumberRange.class)) {
                methods.add(method);
            }
        }


        beforeMethods.forEach(it -> runTest(it, testObj));

        methods.stream().sorted(Comparator.comparingInt(it -> it.getAnnotation(Test.class).order()))
                .forEach(it -> {
                    if (it.isAnnotationPresent(NumberRange.class))
                        for (int param : it.getAnnotation(NumberRange.class).numberRange()) {
                            runTest(it, testObj, param);
                        }
                    else
                        runTest(it, testObj);
                });

        afterMethods.forEach(it -> runTest(it, testObj));
    }

    private static void checkTestMethod(Method method) {
        if (!method.getReturnType().isAssignableFrom(void.class) || method.getParameterCount() != 0) {
            throw new IllegalArgumentException("Метод \"" + method.getName() + "\" должен быть void и не иметь аргументов");
        }
    }
    /**
     * Метод для запуска тестов без передачи параметров в тестовый метод.
     */
    private static void runTest(Method testMethod, Object testObj) {
        try {
            testMethod.invoke(testObj);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось запустить тестовый метод \"" + testMethod.getName() + "\"");
        }
    }

    /**
     * Для задания 4. Метод для запуска тестов с передачей параметров в тестовый метод.
     */
    private static void runTest(Method testMethod, Object testObj, int params) {
        try {
            testMethod.invoke(testObj, params);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось запустить тестовый метод \"" + testMethod.getName() + "\"");
        }
    }
}
