package application.usecases;

import application.repositories.EnrollmentRepository;
import application.repositories.UserRepository;
import domain.entities.Enrollment;
import domain.entities.Student;
import domain.exceptions.BusinessException;

import java.util.List;
import java.util.Optional;

public class CancelarMatriculaUseCase {
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    public CancelarMatriculaUseCase(EnrollmentRepository enrollmentRepository,
                                    UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
    }

    public void execute(String studentEmail, String courseTitle) {
        Optional<domain.entities.User> userOpt = userRepository.findByEmail(studentEmail);
        if (userOpt.isEmpty() || !(userOpt.get() instanceof Student)) {
            throw new BusinessException("Aluno não encontrado: " + studentEmail);
        }

        Student student = (Student) userOpt.get();
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
        
        Optional<Enrollment> enrollmentOpt = enrollments.stream()
            .filter(e -> e.getCourse().getTitle().equals(courseTitle))
            .findFirst();

        if (enrollmentOpt.isEmpty()) {
            throw new BusinessException("Aluno não está matriculado no curso: " + courseTitle);
        }

        enrollmentRepository.remove(enrollmentOpt.get());
    }
}

