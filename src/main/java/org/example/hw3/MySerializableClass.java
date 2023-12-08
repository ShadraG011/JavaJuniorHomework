package org.example.hw3;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class MySerializableClass {
    private static final String filesRoot = "src\\main\\java\\org\\example\\hw3\\files";
    private static Path pathToFile;

    public static String serialize(Object obj) {
        pathToFile = Path.of(filesRoot, obj.getClass().getSimpleName() + "_" + UUID.randomUUID());
        CreateFiles();
        try (
                OutputStream outputStream = Files.newOutputStream(pathToFile);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)
        ) {
            objectOutputStream.writeObject(obj);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return pathToFile.getFileName().toString();
    }


    public static <T> T deSerialize(String fileName, Class<T> tClass, boolean isDeleteFile) {
        pathToFile = Path.of(filesRoot, fileName);
        try (
                InputStream inputStream = Files.newInputStream(pathToFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)
        ) {
            if (isDeleteFile)
                Files.delete(pathToFile);
            return tClass.cast(objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Файл с именем '" + fileName + "' не найден! (Возможно он был прочитан и удален)");
            return null;
        }
    }

    private static void CreateFiles() {
        if (!Files.isDirectory(pathToFile.getParent())) {
            try {
                Files.createDirectory(pathToFile.getParent());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        if (!Files.exists(pathToFile.toAbsolutePath())) {
            try {
                Files.createFile(pathToFile.toAbsolutePath());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
