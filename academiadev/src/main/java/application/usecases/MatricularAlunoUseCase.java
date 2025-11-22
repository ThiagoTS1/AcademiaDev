package application.usecases;

import application.repositories.CourseRepository;
import application.repositories.EnrollmentRepository;
import application.repositories.UserRepository;
import domain.entities.Course;
import domain.entities.Enrollment;
import domain.entities.Student;
import domain.exceptions.EnrollmentException;

import java.util.Optional;

public class MatricularAlunoUseCase {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public MatricularAlunoUseCase(EnrollmentRepository enrollmentRepository,
                                  CourseRepository courseRepository,
                                  UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public void execute(String studentEmail, String courseTitle) {
        Optional<domain.entities.User> userOpt = userRepository.findByEmail(studentEmail);
        if (userOpt.isEmpty() || !(userOpt.get() instanceof Student)) {
            throw new EnrollmentException("Aluno não encontrado: " + studentEmail);
        }

        Student student = (Student) userOpt.get();
        Optional<Course> courseOpt = courseRepository.findByTitle(courseTitle);
        if (courseOpt.isEmpty()) {
            throw new EnrollmentException("Curso não encontrado: " + courseTitle);
        }

        Course course = courseOpt.get();
        
        if (!course.isActive()) {
            throw new EnrollmentException("Curso não está ativo: " + courseTitle);
        }

        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new EnrollmentException("Aluno já está matriculado neste curso");
        }

        int activeEnrollments = enrollmentRepository.countActiveByStudent(student);
        if (!student.canEnroll(activeEnrollments)) {
            throw new EnrollmentException("Plano do aluno não permite mais matrículas ativas");
        }

        Enrollment enrollment = new Enrollment(student, course);
        enrollmentRepository.save(enrollment);
    }
}

