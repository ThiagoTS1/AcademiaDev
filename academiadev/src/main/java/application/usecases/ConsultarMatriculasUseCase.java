package application.usecases;

import application.repositories.EnrollmentRepository;
import application.repositories.UserRepository;
import domain.entities.Enrollment;
import domain.entities.Student;
import domain.exceptions.BusinessException;

import java.util.List;
import java.util.Optional;

public class ConsultarMatriculasUseCase {
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    public ConsultarMatriculasUseCase(EnrollmentRepository enrollmentRepository,
                                      UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
    }

    public List<Enrollment> execute(String studentEmail) {
        Optional<domain.entities.User> userOpt = userRepository.findByEmail(studentEmail);
        if (userOpt.isEmpty() || !(userOpt.get() instanceof Student)) {
            throw new BusinessException("Aluno não encontrado: " + studentEmail);
        }
        Student student = (Student) userOpt.get();
        return enrollmentRepository.findByStudent(student);
    }
}

