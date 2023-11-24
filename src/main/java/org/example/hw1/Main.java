package org.example.hw1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static final Random random = new Random();

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(random.nextInt(1, 101));
        }

        System.out.println("Начальный список" + list);

        double result = list.stream().filter(num -> num % 2 == 0).mapToInt(num -> num).average().orElse(Double.NaN);

        String printResult = !Double.isNaN(result) ?
                String.format("Среднее значение списка: %.2f", result) :
                "Ошибка в нахождении среднего значения четных элементов списка.";

        System.out.printf(printResult);


    }

}
