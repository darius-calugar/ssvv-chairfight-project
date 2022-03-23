package util;

import domain.Student;
import domain.Tema;

import java.io.IOException;
import java.nio.file.Files;

public class Util {
    public static Student makeValidStudent() {
        return new Student("uniqueId", "nume", 1, "mail@mail.mail");
    }

    public static Tema makeValidAssignment() {
        return new Tema("uniqueId", "desc", 1, 1);
    }

    public static String makeTempFile(String inputResource, String extension) throws IOException {
        var tempFile = Files.createTempFile(java.util.UUID.randomUUID().toString(), extension);
        Files.write(tempFile, ClassLoader.getSystemResource(inputResource).openStream().readAllBytes());
        return tempFile.toString();
    }
}
