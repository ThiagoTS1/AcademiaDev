package application.usecases;

import application.repositories.UserRepository;
import domain.entities.Student;
import domain.entities.SubscriptionPlan;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GerarRelatorioAlunosPorPlanoUseCase {
    private final UserRepository userRepository;

    public GerarRelatorioAlunosPorPlanoUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<SubscriptionPlan, List<Student>> execute() {
        return userRepository.findAllStudents().stream()
            .map(student -> (Student) student)
            .collect(Collectors.groupingBy(Student::getSubscriptionPlan));
    }
}

