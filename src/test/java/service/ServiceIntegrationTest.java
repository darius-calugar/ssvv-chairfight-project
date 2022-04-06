package service;

import curent.Curent;
import org.junit.jupiter.api.*;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.io.IOException;

import static util.Util.*;

class ServiceIntegrationTest {
    private Service service;

    @BeforeEach
    void setUp() throws IOException {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(makeTempFile("fisiere/Studenti.xml", "xml"));
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(makeTempFile("fisiere/Teme.xml", "xml"));
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(makeTempFile("fisiere/Note.xml", "xml"));

        Curent.setFilename(makeTempFile("fisiere/DataInceput.txt", "txt"));

        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @Test
    void addStudent_validStudent_Null() {
        var student = makeValidStudent();
        Assertions.assertNull(service.addStudent(student));
    }

    @Test
    void addTema_correctAssignment_Null() {
        var assignment = makeValidAssignment();
        Assertions.assertNull(service.addTema(assignment));
    }

    @Test
    void addNota_correctGrade_GradeValue() {
        var grade = makeValidGrade();
        grade.setIdStudent("1");
        grade.setIdTema("1");
        Assertions.assertEquals(service.addNota(grade, "feedback"), grade.getNota());
    }

    @Test
    void bigBang_Success() {
        var student = makeValidStudent();
        Assertions.assertNull(service.addStudent(student));

        var assignment = makeValidAssignment();
        Assertions.assertNull(service.addTema(assignment));

        var grade = makeValidGrade();
        Assertions.assertEquals(service.addNota(grade, "feedback"), grade.getNota());
    }
}
