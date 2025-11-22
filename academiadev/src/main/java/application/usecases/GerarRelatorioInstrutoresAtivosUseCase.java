package application.usecases;

import application.repositories.CourseRepository;
import domain.entities.Course;

import java.util.Set;
import java.util.stream.Collectors;

public class GerarRelatorioInstrutoresAtivosUseCase {
    private final CourseRepository courseRepository;

    public GerarRelatorioInstrutoresAtivosUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Set<String> execute() {
        return courseRepository.findActiveCourses().stream()
            .map(Course::getInstructorName)
            .collect(Collectors.toSet());
    }
}

