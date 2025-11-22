package infrastructure.persistence;

import application.repositories.CourseRepository;
import domain.entities.Course;
import domain.enums.CourseStatus;
import domain.enums.DifficultyLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CourseRepositoryEmMemoria implements CourseRepository {
    private final Map<String, Course> courses = new ConcurrentHashMap<>();

    @Override
    public void save(Course course) {
        courses.put(course.getTitle(), course);
    }

    @Override
    public Optional<Course> findByTitle(String title) {
        return Optional.ofNullable(courses.get(title));
    }

    @Override
    public List<Course> findAll() {
        return new ArrayList<>(courses.values());
    }

    @Override
    public List<Course> findByDifficultyLevel(DifficultyLevel difficultyLevel) {
        return courses.values().stream()
            .filter(course -> course.getDifficultyLevel() == difficultyLevel)
            .toList();
    }

    @Override
    public List<Course> findActiveCourses() {
        return courses.values().stream()
            .filter(Course::isActive)
            .toList();
    }
}

