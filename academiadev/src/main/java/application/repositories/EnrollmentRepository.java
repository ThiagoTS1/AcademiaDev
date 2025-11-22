package application.repositories;

import domain.entities.Enrollment;
import domain.entities.Student;

import java.util.List;

public interface EnrollmentRepository {
    void save(Enrollment enrollment);
    List<Enrollment> findByStudent(Student student);
    List<Enrollment> findActiveByStudent(Student student);
    int countActiveByStudent(Student student);
    List<Enrollment> findAll();
    void remove(Enrollment enrollment);
    boolean existsByStudentAndCourse(Student student, domain.entities.Course course);
}

