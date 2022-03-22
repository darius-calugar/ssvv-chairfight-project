package service;

import domain.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.io.IOException;
import java.nio.file.Files;

class ServiceTest {
    private Service service;

    Student makeValidStudent() {
        return new Student("uniqueId", "nume", 1, "mail@mail.mail");
    }

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
        var student = makeValidStudent();
        student.setGrupa(20);
        Assertions.assertNull(service.addStudent(student));
    }

    @Test
    void addStudent_negativeGroup_ValidationException() {
        var student = makeValidStudent();
        student.setGrupa(-1);
        Assertions.assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    void addStudent_group0_Success() {
        var student = makeValidStudent();
        student.setGrupa(0);
        Assertions.assertNull(service.addStudent(student));
    }

    @Test
    void addStudent_group1_Success() {
        var student = makeValidStudent();
        student.setGrupa(1);
        Assertions.assertNull(service.addStudent(student));
    }

    @Test
    void addStudent_groupSmallerThanMaxInt_Success() {
        var student = makeValidStudent();
        student.setGrupa(Integer.MAX_VALUE - 1);
        Assertions.assertNull(service.addStudent(student));
    }

    @Test
    void addStudent_groupMaxInt_Success() {
        var student = makeValidStudent();
        student.setGrupa(Integer.MAX_VALUE);
        Assertions.assertNull(service.addStudent(student));
    }

    @Test
    void addStudent_groupLargerThanMaxInt_ValidationException() {
        var student = makeValidStudent();
        student.setGrupa(Integer.MAX_VALUE + 1); // equivalent to -(MAX_VALUE + 1) -> fails at < 0
        Assertions.assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    void addStudent_idNonEmpty_Success() {
        var student = makeValidStudent();
        student.setID("uniqueId");
        Assertions.assertNull(service.addStudent(student));
    }

    @Test
    void addStudent_idNull_ValidationException() {
        var student = makeValidStudent();
        student.setID(null);
        Assertions.assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    void addStudent_idEmpty_ValidationException() {
        var student = makeValidStudent();
        student.setID("");
        Assertions.assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    void addStudent_nameNonEmpty_ValidationException() {
        var student = makeValidStudent();
        student.setNume("nume");
        Assertions.assertNull(service.addStudent(student));
    }

    @Test
    void addStudent_nameNull_ValidationException() {
        var student = makeValidStudent();
        student.setNume(null);
        Assertions.assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    void addStudent_nameEmpty_ValidationException() {
        var student = makeValidStudent();
        student.setNume("");
        Assertions.assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    void addStudent_emailNonEmpty_ValidationException() {
        var student = makeValidStudent();
        student.setEmail("mail@mail.mail");
        Assertions.assertNull(service.addStudent(student));
    }

    @Test
    void addStudent_emailNull_ValidationException() {
        var student = makeValidStudent();
        student.setEmail(null);
        Assertions.assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    void addStudent_emailEmpty_ValidationException() {
        var student = makeValidStudent();
        student.setEmail("");
        Assertions.assertThrows(ValidationException.class, () -> service.addStudent(student));
    }
}
