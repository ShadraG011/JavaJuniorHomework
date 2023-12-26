package org.example.hw5;

import lombok.Getter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


/**
 * 0. Разобраться с написанным кодом в классах Server и Client.
 * 1. Если в начале сообщения есть '@4' - то значит отсылаем сообщеине клиенту с идентификатором 4.
 * 2. Если в начале сообщения нет '@' - значит, это сообщение нужно послать остальным клиентам.
 * 3.* Добавить админское подключение, которое может кикать других клиентов.
 * 3.1 При подключении оно посылает спец. сообщение, подтверждающее, что это - админ.
 * 3.2 Теперь, если админ посылает сообщение kick 4 - то отключаем клиента с идентификатором 4.
 * 4.** Подумать, как лучше структурировать программу (раскидать код по классам).
 */
public class Server {

    public static final int PORT = 8180;
    private static Map<Long, SocketWrapper> clients = new HashMap<>();

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + server.getLocalPort());

            while (true) {

                final Socket client = server.accept();
                SocketWrapper wrapper = new SocketWrapper(client);

                new Thread(() -> {
                    try (Scanner input = wrapper.getInput(); PrintWriter output = wrapper.getOutput()) {
                        String passwordInput = input.nextLine();

                        wrapper.setAdmin(passwordInput);
                        if (wrapper.isAdmin())
                            System.out.println("Подключился администратор[" + wrapper + "]");
                        else
                            System.out.println("Подключился новый клиент[" + wrapper.getSocket().getInetAddress() + "]");
                        clients.put(wrapper.getId(), wrapper);
                        output.println("Подключение успешно. Список всех клиентов: " + clients);


                        while (true) {

                            String clientInput;

                            try {
                                clientInput = input.nextLine();
                            } catch (NoSuchElementException e) { // обработка исключения, если клиент был кикнут администратором
                                System.out.printf("Клиент [%d] был кикнут с сервера.", wrapper.getId());
                                break;
                            }

                            if (wrapper.isAdmin() && clientInput.startsWith("kick")) {
                                long clientId = Long.parseLong(clientInput.substring(clientInput.length() - 1));
                                SocketWrapper kickedClient = clients.values().stream().filter(it -> it.getId() == clientId && !(it.isAdmin())).findFirst().orElse(null);
                                if (kickedClient != null) {
                                    kickedClient.getOutput().println("kick");
                                    clients.values().forEach(it -> {
                                        if (it.getId() != clientId)
                                            it.getOutput().println("Клиент [" + clientId + "] был кикнут администратором");
                                    });
                                    clients.remove(clientId);
                                }
                                continue;
                            }

                            if (Objects.equals("q", clientInput)) {
                                long clientId = wrapper.getId();
                                clients.remove(clientId);
                                clients.values().forEach(it -> it.getOutput().println("Клиент [" + clientId + "] отключился"));
                                break;
                            }


                            if (clientInput.charAt(0) == '@') {
                                // формат сообщения: "@цифра сообщение"
                                long destinationId = Long.parseLong(clientInput.substring(1, 2));
                                SocketWrapper destination = clients.get(destinationId);
                                destination.getOutput().println("|from: @" + wrapper.getId() + "|" + clientInput.substring(2));
                            } else {
                                clients.values().forEach(it -> {
                                    if (it != wrapper)
                                        it.getOutput().println("|Everybody from: @" + wrapper.getId() + "| " + clientInput);
                                });
                            }
                        }
                    }
                }).start();
            }
        }
    }



}


