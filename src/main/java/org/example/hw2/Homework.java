package org.example.hw2;

import org.example.hw2.annotation.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import static org.example.hw2.MyAssertBoolean.*;

public class Homework {

    /**
     * Расширить пример с запуском тестов следующими фичами:
     * 1. Добавить аннотации BeforeEach, AfterEach,
     * которые ставятся над методами void без аргументов и запускаются ДО и ПОСЛЕ всех тестов соответственно.
     * <p>
     * 2. В аннотацию Test добавить параметр order() со значением 0 по умолчанию.
     * Необходимо при запуске тестов фильтровать тесты по этому параметру (от меньшего к большему).
     * Т.е. если есть методы @Test(order = -2) void first, @Test void second, Test(order = 5) void third,
     * то порядок вызовов first -> second -> third
     * <p>
     * 3.* Добавить аннотацию @Skip, которую можно ставить над тест-методами. Если она стоит - то тест не запускается.
     * <p>
     * 4.* При наличии идей, реализовать их и написать об этом в комментарии при сдаче.
     */
    public static void main(String[] args) {
        TestProcessor.runTest(MyClassTest.class);
    }


}

/**
 * Для задания 4. Класс методы которого нужно протестировать
 */
class MyClass {
    public boolean numberIsPowerOfTwo(int number) {
        return number > 0 && (number & -number) == number;
    }
}

/**
 * Класс для создания тестовых методов
 */
class MyClassTest {
    private Constructor<MyClass> myClassConstructor;
    private MyClass myClass;

    /**
     * Для задания 4, в этом методе происходит инициализация класса "MyClass",
     * для будущего тестирования метода "numberIsPowerOfTwo".
     */
    @BeforeEach
    void initMyClass() {
        Class<MyClass> myClassClass = MyClass.class;


        try {
            myClassConstructor = myClassClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }


        try {
            myClass = myClassConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void afterEach() {
        System.out.println("Запущен метод с аннотацией AfterEach");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("Запущен метод с аннотацией BeforeEach");
    }


    /**
     * Для задания 4. Метод для проверки положительного исхода при использовании ряда чисел
     * @param number
     */
    @Test(order = -2)
    @NumberRange(numberRange = {8, 16, 32, 64, 128, 256})
    void numberIsPowerOfTwoTestPositive(int number) {
        System.out.println("numberIsPowerOfTwoTestPositive запущен");
        myAssertTrue(myClass.numberIsPowerOfTwo(number));
    }

    /**
     * Для задания 4. Метод для проверки негативного исхода при использовании ряда чисел
     * @param number
     */
    @Test(order = -1)
    @NumberRange(numberRange = {3, 12, 23, 45, 125, 265})
    void numberIsPowerOfTwoTestNegative(int number) {
        System.out.println("numberIsPowerOfTwoTestNegative запущен");
        myAssertFalse(myClass.numberIsPowerOfTwo(number));
    }

    @Test
    void orderZeroTest() {
        System.out.println("orderZeroTest запущен");

    }

    @Test(order = 1)
    void firstTest() {
        System.out.println("firstTest запущен");
    }

    @Skip
    void skipMethod() {
        System.out.println("Этот метод не должен запускаться");
    }

    @Test(order = 2)
    void secondTest() {
        System.out.println("secondTest запущен");
    }

    @Test(order = 3)
    void thirdTest() {
        System.out.println("thirdTest запущен");
    }

}
