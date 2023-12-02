package org.example.hw2;

public class MyAssertBoolean {
    public static void myAssertTrue(boolean expected) {
        if (expected)
            System.out.println("Тест пройден!");
        else
            System.out.println("Тест не пройден :(");
    }

    public static void myAssertFalse(boolean expected) {
        if (!expected)
            System.out.println("Тест пройден!");
        else
            System.out.println("Тест не пройден :(");
    }
}
