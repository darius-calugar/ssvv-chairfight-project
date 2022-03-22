package service;

import domain.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class ServiceTest {
    private Service service;

    String makeTempFile(String inputResource, String extension) throws IOException {
        var tempFile = Files.createTempFile(java.util.UUID.randomUUID().toString(), extension);
        Files.write(tempFile, ClassLoader.getSystemResource(inputResource).openStream().readAllBytes());
        return tempFile.toString();
    }

    @BeforeEach
    void setUp() throws IOException {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(makeTempFile("fisiere/Studenti.xml", "xml"));
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(makeTempFile("fisiere/Teme.xml", "xml"));
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(makeTempFile("fisiere/Note.xml", "xml"));

        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @Test
    void addStudent_correctGroup_success() {
        Assertions.assertNull(service.addStudent(new Student("uniqueId", "nume", 20, "mail@mail.mail")));
    }

    @Test
    void addStudent_negativeGroup_ValidationException() {
        Assertions.assertThrows(ValidationException.class, () -> service.addStudent(new Student("uniqueId", "nume", -20, "mail@mail.mail")));
    }
}
