package application.usecases;

import application.repositories.UserRepository;
import domain.entities.Student;
import domain.entities.SubscriptionPlan;
import domain.exceptions.BusinessException;

import java.util.Optional;

public class AlterarPlanoAlunoUseCase {
    private final UserRepository userRepository;

    public AlterarPlanoAlunoUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(String studentEmail, SubscriptionPlan newPlan) {
        Optional<domain.entities.User> userOpt = userRepository.findByEmail(studentEmail);
        if (userOpt.isEmpty() || !(userOpt.get() instanceof Student)) {
            throw new BusinessException("Aluno não encontrado: " + studentEmail);
        }
        Student student = (Student) userOpt.get();
        student.changeSubscriptionPlan(newPlan);
    }
}

