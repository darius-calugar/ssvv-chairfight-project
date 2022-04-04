package service;

import domain.Tema;
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

import static util.Util.makeTempFile;
import static util.Util.makeValidAssignment;

public class ServiceWhiteBoxTest {
    private Service service;

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
    void addTema_emptyId_ValidationException() {
        var assignment = makeValidAssignment();
        assignment.setID("");
        Assertions.assertThrows(ValidationException.class, () -> service.addTema(assignment));
    }

    @Test
    void addTema_nullId_ValidationException() {
        var assignment = makeValidAssignment();
        assignment.setID(null);
        Assertions.assertThrows(ValidationException.class, () -> service.addTema(assignment));
    }

    @Test
    void addTema_emptyDescription_ValidationException() {
        var assignment = makeValidAssignment();
        assignment.setDescriere("");
        Assertions.assertThrows(ValidationException.class, () -> service.addTema(assignment));
    }

    @Test
    void addTema_nullDescription_ValidationException() {
        var assignment = makeValidAssignment();
        assignment.setDescriere(null);
        Assertions.assertThrows(ValidationException.class, () -> service.addTema(assignment));
    }

    @Test
    void addTema_deadlineLessThan1_ValidationException() {
        var assignment = makeValidAssignment();
        assignment.setDeadline(0);
        Assertions.assertThrows(ValidationException.class, () -> service.addTema(assignment));
    }

    @Test
    void addTema_deadlineGreaterThan14_ValidationException() {
        var assignment = makeValidAssignment();
        assignment.setDeadline(15);
        Assertions.assertThrows(ValidationException.class, () -> service.addTema(assignment));
    }

    @Test
    void addTema_primireLessThan1_ValidationException() {
        var assignment = makeValidAssignment();
        assignment.setPrimire(0);
        Assertions.assertThrows(ValidationException.class, () -> service.addTema(assignment));
    }

    @Test
    void addTema_primireGreaterThan14_ValidationException() {
        var assignment = makeValidAssignment();
        assignment.setPrimire(15);
        Assertions.assertThrows(ValidationException.class, () -> service.addTema(assignment));
    }

    @Test
    void addTema_existingAssignment_ExistingAssignment() {
        var assignment = makeValidAssignment();
        assignment.setID("1");
        Assertions.assertEquals(
                new Tema("1", "file repository", 2, 1),
                service.addTema(assignment)
        );
    }

    @Test
    void addTema_correctAssignment_Success() {
        var assignment = makeValidAssignment();
        assignment.setPrimire(10);
        Assertions.assertNull(service.addTema(assignment));
    }
}
