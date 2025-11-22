package application.usecases;

import application.repositories.SupportTicketQueue;
import application.repositories.UserRepository;
import domain.entities.SupportTicket;
import domain.entities.User;
import domain.exceptions.BusinessException;

import java.util.Optional;

public class AbrirTicketUseCase {
    private final SupportTicketQueue ticketQueue;
    private final UserRepository userRepository;

    public AbrirTicketUseCase(SupportTicketQueue ticketQueue, UserRepository userRepository) {
        this.ticketQueue = ticketQueue;
        this.userRepository = userRepository;
    }

    public void execute(String userEmail, String title, String message) {
        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        if (userOpt.isEmpty()) {
            throw new BusinessException("Usuário não encontrado: " + userEmail);
        }

        SupportTicket ticket = new SupportTicket(title, message, userOpt.get());
        ticketQueue.addTicket(ticket);
    }
}

