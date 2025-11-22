package infrastructure.persistence;

import application.repositories.EnrollmentRepository;
import domain.entities.Course;
import domain.entities.Enrollment;
import domain.entities.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EnrollmentRepositoryEmMemoria implements EnrollmentRepository {
    private final List<Enrollment> enrollments = new CopyOnWriteArrayList<>();

    @Override
    public void save(Enrollment enrollment) {
        enrollments.add(enrollment);
    }

    @Override
    public List<Enrollment> findByStudent(Student student) {
        return enrollments.stream()
            .filter(e -> e.getStudent().getEmail().equals(student.getEmail()))
            .toList();
    }

    @Override
    public List<Enrollment> findActiveByStudent(Student student) {
        return enrollments.stream()
            .filter(e -> e.getStudent().getEmail().equals(student.getEmail()))
            .filter(Enrollment::isActive)
            .toList();
    }

    @Override
    public int countActiveByStudent(Student student) {
        return (int) enrollments.stream()
            .filter(e -> e.getStudent().getEmail().equals(student.getEmail()))
            .filter(Enrollment::isActive)
            .count();
    }

    @Override
    public List<Enrollment> findAll() {
        return new ArrayList<>(enrollments);
    }

    @Override
    public void remove(Enrollment enrollment) {
        enrollments.remove(enrollment);
    }

    @Override
    public boolean existsByStudentAndCourse(Student student, Course course) {
        return enrollments.stream()
            .anyMatch(e -> e.getStudent().getEmail().equals(student.getEmail()) &&
                          e.getCourse().getTitle().equals(course.getTitle()));
    }
}

