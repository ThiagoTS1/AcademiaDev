package infrastructure.persistence;

import application.repositories.SupportTicketQueue;
import domain.entities.SupportTicket;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

public class SupportTicketQueueEmMemoria implements SupportTicketQueue {
    private final Queue<SupportTicket> queue = new ArrayDeque<>();

    @Override
    public void addTicket(SupportTicket ticket) {
        queue.offer(ticket);
    }

    @Override
    public Optional<SupportTicket> nextTicket() {
        return Optional.ofNullable(queue.poll());
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

