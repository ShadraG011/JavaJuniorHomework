package org.example.hw4;

import jakarta.persistence.NoResultException;
import org.example.hw4.models.Author;
import org.example.hw4.models.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Hibernate {
    /**
     * 2. С помощью JPA(Hibernate) выполнить:
     * 2.1 Описать сущность Book из пункта 1.1
     * 2.2 Создать Session и сохранить в таблицу 10 книг
     * 2.3 Выгрузить список книг какого-то автора
     * <p>
     * 3.* Создать сущность Автор (id biging, name varchar), и в сущности Book сделать поле типа Author (OneToOne)
     * 3.1 * Выгрузить Список книг и убедиться, что поле author заполнено
     * 3.2 ** В классе Author создать поле List<Book>, которое описывает список всех книг этого автора. (OneToMany)
     */
    private static final SessionFactory sessionFactory = new Configuration()
            .configure("hibernate.cfg.xml").buildSessionFactory();

    public static void main(String[] args) {

        addNewBook("Хоббит", "Толкиен");
        addNewBook("Властелин колец", "Толкиен");
        addNewBook("Сильмариллион", "Толкиен");
        addNewBook("1984", "Оруэлл");
        addNewBook("Ферма Доктора Долиттла", "Луфтгин");
        addNewBook("Поющие в терновнике", "Луфтгин");
        addNewBook("Гарри Поттер и философский камень", "Роулинг");
        addNewBook("Гарри Поттер и узник Азкабана", "Роулинг");
        addNewBook("Мастер и Маргарита", "Булгаков");
        addNewBook("Собачье сердце", "Булгаков");
        addNewBook("Тень ветра", "Сарес");
        addNewBook("Игра престолов", "Мартин");
        addNewBook("Атлант расправил плечи", "Рэнд");
        addNewBook("Три товарища", "Ремарк");
        addNewBook("Зеленая миля", "Кинг");
        addNewBook("О дивный новый мир", "Хаксли");
        addNewBook("Алиса в стране чудес", "Кэрролл");
        addNewBook("Преступление и наказание", "Достоевский");
        addNewBook("Война и мир", "Толстой");
        addNewBook("Сто лет одиночества", "Гарсиа Маркес");


        Book bookById = getBookById(7L);
        Book bookByTitle = getBookByTitle("Гарри Поттер и философский камень");

        System.out.println("\nРабота метода 'getBookById': " + bookById);
        System.out.println("\nРабота метода 'getBookByTitle': " + bookByTitle);


        System.out.println("\nРабота метода 'getBooksByAuthor'");
        String authorName = "Толкиен";
        List<Book> books = getBooksByAuthor(authorName);
        if (books != null) {
            System.out.printf("Книги автора %s:\n", authorName);
            books.forEach(System.out::println);
        } else {
            System.out.printf("Книг по автору '%s' не найдено.\n", authorName);
        }


        authorName = "Луфтгин";
        System.out.printf("\nСписок книг автора '%s' до удаления\n", authorName);
        books = getBooksByAuthor(authorName);
        if (books != null) {
            System.out.printf("Книги автора %s:\n", authorName);
            books.forEach(System.out::println);
        } else {
            System.out.printf("Книг по автору '%s' не найдено.\n", authorName);
        }

        String bookTitle = "Ферма Доктора Долиттла";
        deletedBookByTitle(bookTitle);
//        long bookId = 6L;
//        deletedBookById(bookId);

        System.out.printf("\nСписок книг автора '%s' после удаления\n", authorName);
        books = getBooksByAuthor(authorName);
        if (books != null) {
            System.out.printf("Книги автора %s:\n", authorName);
            books.forEach(System.out::println);
        } else {
            System.out.printf("Книг по автору '%s' не найдено.\n", authorName);
        }
    }

    /**
     * Метод для удаления книги по названию
     * @param bookTitle - название книги
     */
    private static void deletedBookByTitle(String bookTitle) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Book book = null;
            try {
                book = session.createQuery("select b from Book b where b.title = :bookTitle", Book.class)
                        .setParameter("bookTitle", bookTitle)
                        .getSingleResult();
            } catch (NoResultException e) {
                System.out.printf("Книги с названием %s не найдено", bookTitle);
            }

            if (book != null) {
                book.getAuthor().getAuthorsBooks().remove(book);
                session.remove(book);
            }
            session.getTransaction().commit();

        }
    }

    /**
     * Метод для удаления книги по ID
     * @param bookId - bookId
     */
    private static void deletedBookById(long bookId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Book book = null;
            try {
                book = session.createQuery("select b from Book b where b.id = :bookId", Book.class)
                        .setParameter("bookId", bookId)
                        .getSingleResult();
            } catch (NoResultException e) {
                System.out.println("null");
            }

            if (book != null) {
                book.getAuthor().getAuthorsBooks().remove(book);
                session.remove(book);
            }
            session.getTransaction().commit();
        }
    }


    /**
     * Получение книги по имени автора
     *
     * @param authorName - имя автора
     * @return список книг
     */
    private static List<Book> getBooksByAuthor(String authorName) {
        List<Book> booksByAuthor = null;
        Author author = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            try {
                author = session.createQuery("select a from Author a where a.name = :authorName", Author.class)
                        .setParameter("authorName", authorName)
                        .getSingleResult();
            } catch (NoResultException e) {
                return null;
            }

            booksByAuthor = author.getAuthorsBooks();

            session.getTransaction().commit();

        }
        return booksByAuthor;
    }

    /**
     * Получение книги по ID
     *
     * @param id - ID книги
     * @return Book
     */
    private static Book getBookById(long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Book book = session.get(Book.class, id);

            session.getTransaction().commit();
            return book;
        }
    }

    private static Book getBookByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Book book = null;
            try {
                book = session.createQuery("select b from Book b where b.title = :title", Book.class)
                        .setParameter("title", title).getSingleResult();
            } catch (NoResultException e) {
                return null;
            }

            session.getTransaction().commit();
            return book;
        }
    }

    /**
     * Создание новой книги и нового автора (при условии, что его нет в таблице) и добавление её в таблицу
     *
     * @param title      - Название книги
     * @param authorName - имя автора
     */
    private static void addNewBook(String title, String authorName) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Author author = null;
            try {
                author = session.createQuery("select a from Author a where a.name = :authorName", Author.class)
                        .setParameter("authorName", authorName).getSingleResult();
            } catch (NoResultException e) {
                System.out.printf("Создание новой записи автора `%s` в таблице `hibernate_authors`, так как её не было найдено\n", authorName);
                author = new Author(authorName);
                author.setAuthorsBooks(new ArrayList<>());
            }

            Book book = new Book(title, author);
            author.getAuthorsBooks().add(book);

            session.persist(author);
            session.persist(book);

            session.getTransaction().commit();
        }
    }


}
