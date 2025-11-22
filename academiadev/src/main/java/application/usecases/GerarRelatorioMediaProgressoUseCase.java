package application.usecases;

import application.repositories.EnrollmentRepository;
import domain.entities.Enrollment;

import java.util.List;

public class GerarRelatorioMediaProgressoUseCase {
    private final EnrollmentRepository enrollmentRepository;

    public GerarRelatorioMediaProgressoUseCase(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public double execute() {
        List<Enrollment> allEnrollments = enrollmentRepository.findAll();
        if (allEnrollments.isEmpty()) {
            return 0.0;
        }
        return allEnrollments.stream()
            .mapToInt(Enrollment::getProgress)
            .average()
            .orElse(0.0);
    }
}

