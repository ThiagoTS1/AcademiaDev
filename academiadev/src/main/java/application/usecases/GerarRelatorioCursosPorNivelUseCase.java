package application.usecases;

import application.repositories.CourseRepository;
import domain.entities.Course;
import domain.enums.DifficultyLevel;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GerarRelatorioCursosPorNivelUseCase {
    private final CourseRepository courseRepository;

    public GerarRelatorioCursosPorNivelUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> execute(DifficultyLevel difficultyLevel) {
        return courseRepository.findByDifficultyLevel(difficultyLevel).stream()
            .sorted(Comparator.comparing(Course::getTitle))
            .collect(Collectors.toList());
    }
}

