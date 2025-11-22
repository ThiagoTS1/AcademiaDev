package infrastructure.ui;

import domain.entities.*;
import domain.enums.CourseStatus;
import domain.enums.DifficultyLevel;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ConsoleView {
    
    public void printWelcome() {
        System.out.println("\n=== AcademiaDev - Plataforma de Cursos Online ===\n");
    }

    public void printAdminMenu() {
        System.out.println("\n--- Menu Administrador ---");
        System.out.println("1. Ativar Curso");
        System.out.println("2. Desativar Curso");
        System.out.println("3. Alterar Plano de Aluno");
        System.out.println("4. Atender Ticket de Suporte");
        System.out.println("5. Gerar Relatórios");
        System.out.println("6. Exportar Dados para CSV");
        System.out.println("7. Consultar Catálogo de Cursos");
        System.out.println("8. Abrir Ticket de Suporte");
        System.out.println("0. Sair");
        System.out.print("\nEscolha uma opção: ");
    }

    public void printStudentMenu() {
        System.out.println("\n--- Menu Aluno ---");
        System.out.println("1. Matricular-se em Curso");
        System.out.println("2. Consultar Matrículas");
        System.out.println("3. Atualizar Progresso");
        System.out.println("4. Cancelar Matrícula");
        System.out.println("5. Consultar Catálogo de Cursos");
        System.out.println("6. Abrir Ticket de Suporte");
        System.out.println("0. Sair");
        System.out.print("\nEscolha uma opção: ");
    }

    public void printReportsMenu() {
        System.out.println("\n--- Relatórios ---");
        System.out.println("1. Cursos por Nível de Dificuldade");
        System.out.println("2. Instrutores de Cursos Ativos");
        System.out.println("3. Alunos Agrupados por Plano");
        System.out.println("4. Média de Progresso Geral");
        System.out.println("5. Aluno com Mais Matrículas Ativas");
        System.out.println("0. Voltar");
        System.out.print("\nEscolha uma opção: ");
    }

    public void printLoginPrompt() {
        System.out.print("Digite seu email: ");
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printError(String error) {
        System.out.println("ERRO: " + error);
    }

    public void printSuccess(String message) {
        System.out.println("✓ " + message);
    }

    public void printCourses(List<Course> courses) {
        if (courses.isEmpty()) {
            System.out.println("Nenhum curso encontrado.");
            return;
        }
        System.out.println("\n--- Cursos ---");
        for (Course course : courses) {
            System.out.printf("Título: %s | Instrutor: %s | Carga Horária: %dh | Nível: %s | Status: %s%n",
                course.getTitle(), course.getInstructorName(), course.getDurationInHours(),
                course.getDifficultyLevel(), course.getStatus());
        }
    }

    public void printEnrollments(List<Enrollment> enrollments) {
        if (enrollments.isEmpty()) {
            System.out.println("Nenhuma matrícula encontrada.");
            return;
        }
        System.out.println("\n--- Matrículas ---");
        for (Enrollment enrollment : enrollments) {
            System.out.printf("Curso: %s | Progresso: %d%% | Status: %s%n",
                enrollment.getCourse().getTitle(), enrollment.getProgress(),
                enrollment.isActive() ? "Ativo" : "Inativo");
        }
    }

    public void printInstructors(Set<String> instructors) {
        if (instructors.isEmpty()) {
            System.out.println("Nenhum instrutor encontrado.");
            return;
        }
        System.out.println("\n--- Instrutores de Cursos Ativos ---");
        instructors.forEach(System.out::println);
    }

    public void printStudentsByPlan(Map<SubscriptionPlan, List<Student>> studentsByPlan) {
        if (studentsByPlan.isEmpty()) {
            System.out.println("Nenhum aluno encontrado.");
            return;
        }
        System.out.println("\n--- Alunos Agrupados por Plano ---");
        studentsByPlan.forEach((plan, students) -> {
            System.out.println("\nPlano: " + plan.getName());
            students.forEach(student -> 
                System.out.println("  - " + student.getName() + " (" + student.getEmail() + ")"));
        });
    }

    public void printAverageProgress(double average) {
        System.out.printf("\n--- Média de Progresso Geral: %.2f%% ---%n", average);
    }

    public void printStudentWithMostEnrollments(Optional<Student> studentOpt) {
        if (studentOpt.isEmpty()) {
            System.out.println("Nenhum aluno encontrado.");
            return;
        }
        Student student = studentOpt.get();
        System.out.println("\n--- Aluno com Mais Matrículas Ativas ---");
        System.out.println("Nome: " + student.getName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Plano: " + student.getSubscriptionPlan().getName());
    }

    public void printTicket(SupportTicket ticket) {
        System.out.println("\n--- Ticket Atendido ---");
        System.out.println("Título: " + ticket.getTitle());
        System.out.println("Mensagem: " + ticket.getMessage());
        System.out.println("Usuário: " + ticket.getUser().getName() + " (" + ticket.getUser().getEmail() + ")");
    }

    public void printCsv(String csv) {
        System.out.println("\n--- CSV Exportado ---");
        System.out.println(csv);
    }

    public void printDifficultyLevels() {
        System.out.println("\nNíveis de Dificuldade:");
        System.out.println("1. BEGINNER");
        System.out.println("2. INTERMEDIATE");
        System.out.println("3. ADVANCED");
        System.out.print("Escolha: ");
    }

    public void printSubscriptionPlans() {
        System.out.println("\nPlanos:");
        System.out.println("1. BasicPlan");
        System.out.println("2. PremiumPlan");
        System.out.print("Escolha: ");
    }
}

