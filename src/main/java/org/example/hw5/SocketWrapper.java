package org.example.hw5;


import lombok.Getter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

@Getter
public class SocketWrapper implements AutoCloseable {
    private static long clientIdCounter = 1L;

    private final long id;
    private final Socket socket;
    private final Scanner input;
    private final PrintWriter output;
    private boolean isAdmin;

    SocketWrapper(Socket socket) throws IOException {
        this.id = clientIdCounter++;
        this.socket = socket;
        this.input = new Scanner(socket.getInputStream());
        this.output = new PrintWriter(socket.getOutputStream(), true);
    }

    public void setAdmin(String password) {
        isAdmin = password.equals("admin");
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }

    @Override
    public String toString() {
        return String.format("%s", socket.getInetAddress().toString());
    }
}
