package main;

import application.repositories.CourseRepository;
import application.repositories.EnrollmentRepository;
import application.repositories.UserRepository;
import domain.entities.*;
import domain.enums.DifficultyLevel;

public class InitialData {
    
    public static void populate(CourseRepository courseRepository,
                               UserRepository userRepository,
                               EnrollmentRepository enrollmentRepository) {
        Course course1 = new Course("Java Básico", "Introdução à programação Java", 
            "João Silva", 40, DifficultyLevel.BEGINNER);
        Course course2 = new Course("Java Avançado", "Programação avançada em Java", 
            "Maria Santos", 60, DifficultyLevel.ADVANCED);
        Course course3 = new Course("Spring Framework", "Desenvolvimento com Spring", 
            "Pedro Costa", 50, DifficultyLevel.INTERMEDIATE);
        Course course4 = new Course("Algoritmos e Estruturas de Dados", 
            "Fundamentos de algoritmos", "Ana Oliveira", 45, DifficultyLevel.INTERMEDIATE);
        Course course5 = new Course("Banco de Dados", "SQL e modelagem de dados", 
            "Carlos Mendes", 35, DifficultyLevel.BEGINNER);
        
        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);
        courseRepository.save(course4);
        courseRepository.save(course5);
        
        Admin admin1 = new Admin("Admin Principal", "admin@academiadev.com");
        Admin admin2 = new Admin("Admin Secundário", "admin2@academiadev.com");
        
        userRepository.save(admin1);
        userRepository.save(admin2);
        
        Student student1 = new Student("Aluno Um", "aluno1@email.com", new BasicPlan());
        Student student2 = new Student("Aluno Dois", "aluno2@email.com", new PremiumPlan());
        Student student3 = new Student("Aluno Três", "aluno3@email.com", new BasicPlan());
        Student student4 = new Student("Aluno Quatro", "aluno4@email.com", new PremiumPlan());
        
        userRepository.save(student1);
        userRepository.save(student2);
        userRepository.save(student3);
        userRepository.save(student4);
        
        Enrollment enrollment1 = new Enrollment(student1, course1);
        enrollment1.updateProgress(50);
        enrollmentRepository.save(enrollment1);
        
        Enrollment enrollment2 = new Enrollment(student1, course5);
        enrollment2.updateProgress(30);
        enrollmentRepository.save(enrollment2);
        
        Enrollment enrollment3 = new Enrollment(student2, course1);
        enrollment3.updateProgress(100);
        enrollmentRepository.save(enrollment3);
        
        Enrollment enrollment4 = new Enrollment(student2, course2);
        enrollment4.updateProgress(75);
        enrollmentRepository.save(enrollment4);
        
        Enrollment enrollment5 = new Enrollment(student2, course3);
        enrollment5.updateProgress(60);
        enrollmentRepository.save(enrollment5);
        
        Enrollment enrollment6 = new Enrollment(student2, course4);
        enrollment6.updateProgress(40);
        enrollmentRepository.save(enrollment6);
        
        Enrollment enrollment7 = new Enrollment(student3, course1);
        enrollment7.updateProgress(20);
        enrollmentRepository.save(enrollment7);
        
        Enrollment enrollment8 = new Enrollment(student4, course3);
        enrollment8.updateProgress(90);
        enrollmentRepository.save(enrollment8);
    }
}

