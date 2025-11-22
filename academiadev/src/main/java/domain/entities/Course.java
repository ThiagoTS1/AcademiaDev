package domain.entities;

import domain.enums.CourseStatus;
import domain.enums.DifficultyLevel;

public class Course {
    private String title;
    private String description;
    private String instructorName;
    private int durationInHours;
    private DifficultyLevel difficultyLevel;
    private CourseStatus status;

    public Course(String title, String description, String instructorName, 
                  int durationInHours, DifficultyLevel difficultyLevel) {
        this.title = title;
        this.description = description;
        this.instructorName = instructorName;
        this.durationInHours = durationInHours;
        this.difficultyLevel = difficultyLevel;
        this.status = CourseStatus.ACTIVE;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public int getDurationInHours() {
        return durationInHours;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void activate() {
        this.status = CourseStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = CourseStatus.INACTIVE;
    }

    public boolean isActive() {
        return status == CourseStatus.ACTIVE;
    }
}

