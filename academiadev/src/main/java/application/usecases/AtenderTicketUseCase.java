package application.usecases;

import application.repositories.SupportTicketQueue;
import domain.entities.SupportTicket;
import domain.exceptions.BusinessException;

import java.util.Optional;

public class AtenderTicketUseCase {
    private final SupportTicketQueue ticketQueue;

    public AtenderTicketUseCase(SupportTicketQueue ticketQueue) {
        this.ticketQueue = ticketQueue;
    }

    public SupportTicket execute() {
        Optional<SupportTicket> ticketOpt = ticketQueue.nextTicket();
        if (ticketOpt.isEmpty()) {
            throw new BusinessException("Não há tickets na fila");
        }
        return ticketOpt.get();
    }
}

