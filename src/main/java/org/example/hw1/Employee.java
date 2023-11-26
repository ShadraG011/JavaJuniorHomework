package org.example.hw1;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 2. Создать класс Employee (Сотрудник) с полями: String name, int age, double salary, String department
 * * 2.1 Создать список из 10-20 сотрудников
 * * 2.2 Вывести список всех различных отделов (department) по списку сотрудников
 * * 2.3 Всем сотрудникам, чья зарплата меньше 10_000, повысить зарплату на 20%
 * * 2.4 * Из списка сотрудников с помощью стрима создать Map<String, List<Employee>> с отделами и сотрудниками внутри отдела
 * * 2.5 * Из списка сотрудников с помощью стрима создать Map<String, Double> с отделами и средней зарплатой внутри отдела
 */
public class Employee {

    //region Methods

    /**
     * 2.5 * Из списка сотрудников с помощью стрима создать Map<String, Double> с отделами и средней зарплатой внутри отдела
     */
    public void gatAverageSalaryInDepartment() {
        System.out.println("============== Средняя зарплата по отделам ==============");
        Map<String, Double> averageSalaryInDepartment = new HashMap<>();

        employees.stream()
                .forEach(employee -> {
                    String employeeDepartment = employee.getDepartment();
                    averageSalaryInDepartment.put(employeeDepartment, employees.stream()
                            .filter(tmpEmployee -> tmpEmployee.getDepartment().equals(employeeDepartment))
                            .mapToDouble(Employee::getSalary).average().orElse(-1)
                    );
                });

        for (String key : averageSalaryInDepartment.keySet()) {
            System.out.printf("\n" + key + ":\nСредняя зарплата в отделе: %.2f\n", averageSalaryInDepartment.get(key));
        }
        System.out.println("===========================================================");
    }

    /**
     * 2.4 * Из списка сотрудников с помощью стрима создать Map<String, List<Employee>> с отделами и сотрудниками внутри отдела
     */
    public void getEmployeesInDepartment() {
        System.out.println("============== Отделы с сотрудниками ==============");
        Map<String, List<Employee>> employeesInToDepartment = new HashMap<>();

        employees.stream().forEach(employee -> {
            String tmpDepartment = employee.getDepartment();
            employeesInToDepartment.put(tmpDepartment, employees.stream().filter(employee2 -> employee2.department.equals(tmpDepartment)).toList());
        });

        for (String key : employeesInToDepartment.keySet()) {
            String employeeList = employeesInToDepartment.get(key).stream().map(Employee::toString).collect(Collectors.joining("\n"));
            System.out.printf("\n" + key + ":\n%s\n", employeeList);
        }
        System.out.println("=======================================================");
    }

    /**
     * Показать список всех сотрудников
     */
    public void viewAllEmployees() {
        System.out.println("============== Список всех сотрудников ==============");
        employees.stream().forEach(System.out::println);
        System.out.println("=====================================================");
    }


    /**
     * 2.3 Всем сотрудникам, чья зарплата меньше 10_000, повысить зарплату на 20%
     */
    public void raiseSalary() {
        System.out.println("============== Сотрудники с повышенной зарплатой ==============");

//        employees.stream().filter(employee -> employee.getSalary() < 10_000).mapToDouble(employee -> (employee.getSalary() * 1.2)); // можно изменить значения таким образом, но для красивого вывода в консоль я решил использовать кодовый блок описанный ниже

        employees.stream().forEach(employee -> {
            if (employee.getSalary() < 10_000) {
                double newSalary = employee.getSalary() * 1.2;
                employee.setSalary(newSalary);
                System.out.println("\u001B[32m" + employee + "\u001B[0m");
            } else {
                System.out.println(employee);
            }
        });
        System.out.println("===============================================================");
    }

    /**
     * 2.2 Вывести список всех различных отделов (department) по списку сотрудников
     */
    public void viewDepartments() {
        System.out.println("============== Список различных отделов ==============");
        Set<String> setDepartments = employees.stream().map(Employee::getDepartment)
                .collect(Collectors.toCollection(HashSet::new));
        setDepartments.forEach(department -> System.out.println("* " + department));
        System.out.println("======================================================");
    }

    //endregion

    //region Constructors

    /**
     * 2.1 Создать список из 10-20 сотрудников
     */
    public Employee() {
        employees = List.of(
                new Employee("Джон", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "Разработка"),
                new Employee("Мартин", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "Парсинг"),
                new Employee("Бэн", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "Бухгалтерия"),
                new Employee("Питер", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "Отдел тестирования"),
                new Employee("Джон", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "Отдел тестирования"),
                new Employee("Викрам", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "Отдел написания технической литературы"),
                new Employee("Моника", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "HR отдел"),
                new Employee("Мэлман", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "Поддержка пользователей"),
                new Employee("Глория", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "Поддержка пользователей"),
                new Employee("Энтони", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "Разработка"),
                new Employee("Пеппер", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "Парсинг"),
                new Employee("Клара", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "Бухгалтерия"),
                new Employee("Джек", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "Отдел тестирования"),
                new Employee("Фредди", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "Отдел тестирования"),
                new Employee("Оскар", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "Отдел написания технической литературы"),
                new Employee("Сьюзи", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "HR отдел"),
                new Employee("Лили", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "Поддержка пользователей"),
                new Employee("Брюс", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "Отдел написания технической литературы"),
                new Employee("Джессика", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "HR отдел"),
                new Employee("Анджелина", random.nextInt(18, 66), random.nextInt(5_000, 50_000), "Поддержка пользователей")
        );
    }

    public Employee(String name, int age, double salary, String department) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.department = department;
    }
    //endregion

    //region GetterAndSetter (toString)
    @Override
    public String toString() {
        return String.format("|Работник: %s | Возраст: %d | Зарплата: %.2f | Отдел: %s|", name, age, salary, department);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
    //endregion

    //region Fields
    private final Random random = new Random();
    private List<Employee> employees;
    private String name;
    private int age;
    private double salary;
    private String department;
    //endregion
}
