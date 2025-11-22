package application.usecases;

import application.repositories.CourseRepository;
import domain.entities.Course;

import java.util.List;

public class ConsultarCatalogoUseCase {
    private final CourseRepository courseRepository;

    public ConsultarCatalogoUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> execute() {
        return courseRepository.findActiveCourses();
    }
}

