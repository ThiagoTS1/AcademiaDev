package application.repositories;

import domain.entities.Course;
import domain.enums.DifficultyLevel;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    void save(Course course);
    Optional<Course> findByTitle(String title);
    List<Course> findAll();
    List<Course> findByDifficultyLevel(DifficultyLevel difficultyLevel);
    List<Course> findActiveCourses();
}

