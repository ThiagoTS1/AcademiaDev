package application.usecases;

import application.repositories.EnrollmentRepository;
import application.repositories.UserRepository;
import domain.entities.Enrollment;
import domain.entities.Student;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class GerarRelatorioAlunoMaisMatriculasUseCase {
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    public GerarRelatorioAlunoMaisMatriculasUseCase(EnrollmentRepository enrollmentRepository,
                                                    UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
    }

    public Optional<Student> execute() {
        List<Student> students = userRepository.findAllStudents().stream()
            .map(user -> (Student) user)
            .toList();

        return students.stream()
            .max(Comparator.comparingInt(student -> 
                enrollmentRepository.countActiveByStudent(student)));
    }
}

