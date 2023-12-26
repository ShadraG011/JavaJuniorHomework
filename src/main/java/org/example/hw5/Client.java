package org.example.hw5;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        final Boolean[] isConnect = {true};

        final Socket client = new Socket("localhost", Server.PORT);

        // получение
        new Thread(() -> {
            try (Scanner input = new Scanner(client.getInputStream())) {
                while (true) {
                    String msgFromServer = input.nextLine();
                    if (msgFromServer.equals("kick")) {
                        System.out.println("Вы были кикнуты с сервера... (Для выхода нажмите Enter)");
                        isConnect[0] = false;
                        break;
                    }
                    System.out.println(msgFromServer);
                }
            } catch (Exception e) {
                System.out.println("Вы отключились от сервера");
            }
        }).start();

        // отправка
        new Thread(() -> {
            try (PrintWriter output = new PrintWriter(client.getOutputStream(), true)) {
                Scanner consoleScanner = new Scanner(System.in);
                System.out.print("Введите пароль: ");
                while (true) {
                    if(!isConnect[0]) {
                        client.close();
                        break;
                    }
                    String consoleInput = consoleScanner.nextLine();
                    output.println(consoleInput);
                    if (Objects.equals("q", consoleInput)) {
                        client.close();
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
