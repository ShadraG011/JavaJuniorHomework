package org.example.hw3;

import java.io.Serializable;


/**
 * Написать класс с двумя методами:
 * <p>
 * 1. принимает объекты, имплементирующие интерфейс serializable, и сохраняющие их в файл.
 * Название файла - class.getName() + "_" + UUID.randomUUID().toString()
 * <p>
 * 2. принимает строку вида class.getName() + "_" + UUID.randomUUID().toString()
 * и загружает объект из файла и удаляет этот файл.
 * <p>
 * Что делать в ситуациях, когда файла нет или в нем лежит некорректные данные - подумать самостоятельно.
 **/

public class Program {
    public static void main(String[] args) {
        Integer number = 5;
        String str = "Hello, world!";
        Double dbl = 2.0;
        Person person = new Person("Николай", 20);

        //region Сериализация объектов
        String integerFileName = MySerializableClass.serialize(number);
        String stringFileName = MySerializableClass.serialize(str);
        String doubleFileName = MySerializableClass.serialize(dbl);
        String personFileName = MySerializableClass.serialize(person);
        //endregion


        //region Десериализация объектов
        Integer resulInteger = MySerializableClass.deSerialize(integerFileName, Integer.class, true);
        Integer errorInteger = MySerializableClass.deSerialize(integerFileName, Integer.class, true);
        String resultString = MySerializableClass.deSerialize(stringFileName, String.class, false);
        Double resultDouble = MySerializableClass.deSerialize(doubleFileName, Double.class, true);
        Person resultPerson = MySerializableClass.deSerialize(personFileName, Person.class, false);


        System.out.println(resulInteger);
        System.out.println(errorInteger);
        System.out.println(resultString);
        System.out.println(resultDouble);
        System.out.println(resultPerson);
        //endregion
    }
}

class Person implements Serializable {
    private final String name;
    private final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
