package org.example.hw1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class Homework {


    public static void main(String[] args) {

        //region Task1
        System.out.println("\t\t\t\t\t\tЗадание 1");
        Homework homework = new Homework();
        homework.task1();
        //endregion

        //region Task2
        System.out.println("\n\t\t\t\t\t\tЗадание 2");
        Employee mainEmployee = new Employee();
        mainEmployee.viewDepartments();
        System.out.println();
        mainEmployee.viewAllEmployees();
        System.out.println();
        mainEmployee.raiseSalary();
        System.out.println();
        mainEmployee.getEmployeesInDepartment();
        System.out.println();
        mainEmployee.gatAverageSalaryInDepartment();
        //endregion

    }

    /**
     * 1. Создать список из 1_000 рандомных чисел от 1 до 1_000_000
     * * 1.1 Найти максимальное
     * * 2.2 Все числа, большие, чем 500_000, умножить на 5, отнять от них 150 и просуммировать
     * * 2.3 Найти количество чисел, квадрат которых меньше, чем 100_000
     */
    public void task1() {
        List<Integer> list = Stream.generate(() -> ThreadLocalRandom.current().nextInt(1, 1_000_000_001))
                .limit(1000)
                .collect(Collectors.toCollection(ArrayList::new));

        //1.1 Найти максимальное
        int maxNumber = list.stream().mapToInt(num -> num).max().orElse(-1);
        System.out.println("Максимальное число в списке: " + maxNumber);

        //2.2 Все числа, большие, чем 500_000, умножить на 5, отнять от них 150 и просуммировать
        int sumNumbers = list.stream().filter(num -> num > 500_000).mapToInt(num -> (num * 5) - 150).sum();
        System.out.println("Сумма чисел в списке, которые больше 500_000 (умноженных на 5 и с вычетом 150): " + sumNumbers);

        //2.3 Найти количество чисел, квадрат которых меньше, чем 100_000
        int squareNumbers = list.stream().filter(num -> ((long) num * num < 100_000)).toArray().length;
        System.out.println("Количество чисел в списке, квадрат которых меньше 100_000: " + squareNumbers);
    }
}
