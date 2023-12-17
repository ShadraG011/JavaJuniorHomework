package org.example.hw4;

import java.sql.*;

public class Jdbc {


    public static void main(String[] args) {

        Jdbc jdbc = new Jdbc();

        try (Connection connection = DriverManager.getConnection("URL", "USER", "PASSWORD");) {
            jdbc.createTable(connection);
            jdbc.fillTable(connection);
            jdbc.addNewBookInTable(connection, "Властелин колец 1", "Толкиен");
            jdbc.addNewBookInTable(connection, "Властелин колец 2", "Толкиен");
            jdbc.addNewBookInTable(connection, "Властелин колец 3", "Толкиен");
            jdbc.addNewBookInTable(connection, "Хоббит", "Толкиен");
            ResultSet rs = jdbc.getBookByAuthor(connection, "Толкиен");
            jdbc.printResult(rs);
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    /**
     * 1. С помощью JDBC выполнить:
     * 1.1 Создать таблицу book с колонками id bigint, name varchar, author varchar, ...
     */
    public void createTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("""
                create table if not exists jdbc_books(
                 id bigint not null primary key auto_increment,
                 title varchar(255) not null,
                 author varchar(255) not null 
                )
                """);
        statement.close();
    }

    /**
     * 1.2 Добавить в таблицу 10 книг
     */
    public void fillTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("""
                insert into java_junior_hw.jdbc_books (title, author)
                values
                    ("Book #1", "Author #1"),
                    ("Book #2", "Author #2"),
                    ("Book #3", "Author #3"),
                    ("Book #4", "Author #4"),
                    ("Book #5", "Author #5"),
                    ("Book #6", "Author #6"),
                    ("Book #7", "Author #7"),
                    ("Book #8", "Author #8"),
                    ("Book #9", "Author #9"),
                    ("Book #10", "Author #10")
                """);
        statement.close();
    }

    public void addNewBookInTable(Connection connection, String title, String author) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("""
                insert into jdbc_books(title, author)
                values (?, ?)
                """);
        preparedStatement.setString(1, title);
        preparedStatement.setString(2, author);
        preparedStatement.executeUpdate();
    }

    /**
     * 1.3 Сделать запрос select from book where author = 'какое-то имя' и прочитать его с помощью ResultSet
     */
    public ResultSet getBookByAuthor(Connection connection, String author) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("""
                select * from jdbc_books where author = ?;
                """);
        preparedStatement.setString(1, author);
        return preparedStatement.executeQuery();
    }

    public void printResult(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            System.out.println(String.format("| Название книги: '%s' | Автор: '%s' | Номер книги в магазине: '%d' |",
                    resultSet.getString("title"),
                    resultSet.getString("author"),
                    resultSet.getInt("id")
            ));

        }

    }
}
