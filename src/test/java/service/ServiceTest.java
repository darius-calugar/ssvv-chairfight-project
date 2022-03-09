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

class ServiceTest {
    private Service service;

    @BeforeEach
    void setUp() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        StudentXMLRepo studentXMLRepository = new StudentXMLRepo("fisiere/Studenti.xml");
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo("fisiere/Teme.xml");
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo("fisiere/Note.xml");
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @Test
    void addStudent_correctGroup_success() {
        Assertions.assertNotNull(service.addStudent(new Student("uniqueId", "nume", 20, "mail@mail.mail")));
    }

    @Test
    void addStudent_negativeGroup_ValidationException() {
        Assertions.assertThrows(ValidationException.class, () -> service.addStudent(new Student("uniqueId", "nume", 20, "mail@mail.mail")));
    }
}
