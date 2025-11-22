package application.usecases;

import application.repositories.CourseRepository;
import domain.entities.Course;
import domain.exceptions.BusinessException;

import java.util.Optional;

public class DesativarCursoUseCase {
    private final CourseRepository courseRepository;

    public DesativarCursoUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void execute(String courseTitle) {
        Optional<Course> courseOpt = courseRepository.findByTitle(courseTitle);
        if (courseOpt.isEmpty()) {
            throw new BusinessException("Curso não encontrado: " + courseTitle);
        }
        courseOpt.get().deactivate();
    }
}

