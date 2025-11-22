package domain.entities;

import domain.entities.User;

public class SupportTicket {
    private String title;
    private String message;
    private User user;

    public SupportTicket(String title, String message, User user) {
        this.title = title;
        this.message = message;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}

