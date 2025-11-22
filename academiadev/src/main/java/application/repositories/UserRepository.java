package application.repositories;

import domain.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    List<User> findAllStudents();
}

