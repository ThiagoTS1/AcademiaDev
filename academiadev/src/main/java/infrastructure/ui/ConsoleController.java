package infrastructure.ui;

import application.usecases.*;
import application.repositories.*;
import domain.entities.*;
import domain.enums.DifficultyLevel;
import domain.exceptions.BusinessException;
import domain.exceptions.EnrollmentException;
import infrastructure.utils.GenericCsvExporter;

import java.util.*;

public class ConsoleController {
    private final ConsoleView view;
    private final Scanner scanner;
    private final UserRepository userRepository;
    private User currentUser;
    
    private final AtivarCursoUseCase ativarCursoUseCase;
    private final DesativarCursoUseCase desativarCursoUseCase;
    private final AlterarPlanoAlunoUseCase alterarPlanoAlunoUseCase;
    private final MatricularAlunoUseCase matricularAlunoUseCase;
    private final ConsultarMatriculasUseCase consultarMatriculasUseCase;
    private final AtualizarProgressoUseCase atualizarProgressoUseCase;
    private final CancelarMatriculaUseCase cancelarMatriculaUseCase;
    private final AbrirTicketUseCase abrirTicketUseCase;
    private final AtenderTicketUseCase atenderTicketUseCase;
    private final ConsultarCatalogoUseCase consultarCatalogoUseCase;
    private final GerarRelatorioCursosPorNivelUseCase relatorioCursosPorNivelUseCase;
    private final GerarRelatorioInstrutoresAtivosUseCase relatorioInstrutoresAtivosUseCase;
    private final GerarRelatorioAlunosPorPlanoUseCase relatorioAlunosPorPlanoUseCase;
    private final GerarRelatorioMediaProgressoUseCase relatorioMediaProgressoUseCase;
    private final GerarRelatorioAlunoMaisMatriculasUseCase relatorioAlunoMaisMatriculasUseCase;

    public ConsoleController(ConsoleView view,
                            UserRepository userRepository,
                            AtivarCursoUseCase ativarCursoUseCase,
                            DesativarCursoUseCase desativarCursoUseCase,
                            AlterarPlanoAlunoUseCase alterarPlanoAlunoUseCase,
                            MatricularAlunoUseCase matricularAlunoUseCase,
                            ConsultarMatriculasUseCase consultarMatriculasUseCase,
                            AtualizarProgressoUseCase atualizarProgressoUseCase,
                            CancelarMatriculaUseCase cancelarMatriculaUseCase,
                            AbrirTicketUseCase abrirTicketUseCase,
                            AtenderTicketUseCase atenderTicketUseCase,
                            ConsultarCatalogoUseCase consultarCatalogoUseCase,
                            GerarRelatorioCursosPorNivelUseCase relatorioCursosPorNivelUseCase,
                            GerarRelatorioInstrutoresAtivosUseCase relatorioInstrutoresAtivosUseCase,
                            GerarRelatorioAlunosPorPlanoUseCase relatorioAlunosPorPlanoUseCase,
                            GerarRelatorioMediaProgressoUseCase relatorioMediaProgressoUseCase,
                            GerarRelatorioAlunoMaisMatriculasUseCase relatorioAlunoMaisMatriculasUseCase) {
        this.view = view;
        this.scanner = new Scanner(System.in);
        this.userRepository = userRepository;
        this.ativarCursoUseCase = ativarCursoUseCase;
        this.desativarCursoUseCase = desativarCursoUseCase;
        this.alterarPlanoAlunoUseCase = alterarPlanoAlunoUseCase;
        this.matricularAlunoUseCase = matricularAlunoUseCase;
        this.consultarMatriculasUseCase = consultarMatriculasUseCase;
        this.atualizarProgressoUseCase = atualizarProgressoUseCase;
        this.cancelarMatriculaUseCase = cancelarMatriculaUseCase;
        this.abrirTicketUseCase = abrirTicketUseCase;
        this.atenderTicketUseCase = atenderTicketUseCase;
        this.consultarCatalogoUseCase = consultarCatalogoUseCase;
        this.relatorioCursosPorNivelUseCase = relatorioCursosPorNivelUseCase;
        this.relatorioInstrutoresAtivosUseCase = relatorioInstrutoresAtivosUseCase;
        this.relatorioAlunosPorPlanoUseCase = relatorioAlunosPorPlanoUseCase;
        this.relatorioMediaProgressoUseCase = relatorioMediaProgressoUseCase;
        this.relatorioAlunoMaisMatriculasUseCase = relatorioAlunoMaisMatriculasUseCase;
    }

    public void run() {
        view.printWelcome();
        authenticate();
        
        if (currentUser == null) {
            view.printError("Falha na autenticação. Encerrando aplicação.");
            return;
        }

        boolean running = true;
        while (running) {
            try {
                if (currentUser instanceof Admin) {
                    view.printAdminMenu();
                    int option = scanner.nextInt();
                    scanner.nextLine();
                    running = handleAdminOption(option);
                } else if (currentUser instanceof Student) {
                    view.printStudentMenu();
                    int option = scanner.nextInt();
                    scanner.nextLine();
                    running = handleStudentOption(option);
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                view.printError("Opção inválida. Digite um número.");
            } catch (Exception e) {
                view.printError(e.getMessage());
            }
        }
    }

    private void authenticate() {
        view.printLoginPrompt();
        String email = scanner.nextLine();
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            view.printError("Usuário não encontrado: " + email);
            currentUser = null;
            return;
        }
        
        currentUser = userOpt.get();
        String role = currentUser instanceof Admin ? "Administrador" : "Aluno";
        view.printSuccess("Autenticação realizada. Bem-vindo, " + currentUser.getName() + " (" + role + ")!");
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    private boolean handleAdminOption(int option) {
        switch (option) {
            case 1 -> handleAtivarCurso();
            case 2 -> handleDesativarCurso();
            case 3 -> handleAlterarPlanoAluno();
            case 4 -> handleAtenderTicket();
            case 5 -> handleRelatorios();
            case 6 -> handleExportarCsv();
            case 7 -> handleConsultarCatalogo();
            case 8 -> handleAbrirTicket();
            case 0 -> {
                return false;
            }
            default -> view.printError("Opção inválida.");
        }
        return true;
    }

    private boolean handleStudentOption(int option) {
        switch (option) {
            case 1 -> handleMatricularAluno();
            case 2 -> handleConsultarMatriculas();
            case 3 -> handleAtualizarProgresso();
            case 4 -> handleCancelarMatricula();
            case 5 -> handleConsultarCatalogo();
            case 6 -> handleAbrirTicket();
            case 0 -> {
                return false;
            }
            default -> view.printError("Opção inválida.");
        }
        return true;
    }

    private void handleAtivarCurso() {
        view.printMessage("Digite o título do curso:");
        String title = scanner.nextLine();
        try {
            ativarCursoUseCase.execute(title);
            view.printSuccess("Curso ativado com sucesso!");
        } catch (BusinessException e) {
            view.printError(e.getMessage());
        }
    }

    private void handleDesativarCurso() {
        view.printMessage("Digite o título do curso:");
        String title = scanner.nextLine();
        try {
            desativarCursoUseCase.execute(title);
            view.printSuccess("Curso desativado com sucesso!");
        } catch (BusinessException e) {
            view.printError(e.getMessage());
        }
    }

    private void handleAlterarPlanoAluno() {
        view.printMessage("Digite o email do aluno:");
        String email = scanner.nextLine();
        view.printSubscriptionPlans();
        int planOption = scanner.nextInt();
        scanner.nextLine();
        
        SubscriptionPlan plan = planOption == 1 ? new BasicPlan() : new PremiumPlan();
        try {
            alterarPlanoAlunoUseCase.execute(email, plan);
            view.printSuccess("Plano alterado com sucesso!");
        } catch (BusinessException e) {
            view.printError(e.getMessage());
        }
    }

    private void handleMatricularAluno() {
        view.printMessage("Digite o título do curso:");
        String courseTitle = scanner.nextLine();
        try {
            matricularAlunoUseCase.execute(currentUser.getEmail(), courseTitle);
            view.printSuccess("Matrícula realizada com sucesso!");
        } catch (EnrollmentException e) {
            view.printError(e.getMessage());
        }
    }

    private void handleConsultarMatriculas() {
        try {
            List<Enrollment> enrollments = consultarMatriculasUseCase.execute(currentUser.getEmail());
            view.printEnrollments(enrollments);
        } catch (BusinessException e) {
            view.printError(e.getMessage());
        }
    }

    private void handleAtualizarProgresso() {
        view.printMessage("Digite o título do curso:");
        String courseTitle = scanner.nextLine();
        view.printMessage("Digite o progresso (0-100):");
        int progress = scanner.nextInt();
        scanner.nextLine();
        try {
            atualizarProgressoUseCase.execute(currentUser.getEmail(), courseTitle, progress);
            view.printSuccess("Progresso atualizado com sucesso!");
        } catch (BusinessException e) {
            view.printError(e.getMessage());
        }
    }

    private void handleCancelarMatricula() {
        view.printMessage("Digite o título do curso:");
        String courseTitle = scanner.nextLine();
        try {
            cancelarMatriculaUseCase.execute(currentUser.getEmail(), courseTitle);
            view.printSuccess("Matrícula cancelada com sucesso!");
        } catch (BusinessException e) {
            view.printError(e.getMessage());
        }
    }

    private void handleAbrirTicket() {
        view.printMessage("Digite o título do ticket:");
        String title = scanner.nextLine();
        view.printMessage("Digite a mensagem:");
        String message = scanner.nextLine();
        try {
            abrirTicketUseCase.execute(currentUser.getEmail(), title, message);
            view.printSuccess("Ticket criado com sucesso!");
        } catch (BusinessException e) {
            view.printError(e.getMessage());
        }
    }

    private void handleAtenderTicket() {
        try {
            SupportTicket ticket = atenderTicketUseCase.execute();
            view.printTicket(ticket);
        } catch (BusinessException e) {
            view.printError(e.getMessage());
        }
    }

    private void handleConsultarCatalogo() {
        List<Course> courses = consultarCatalogoUseCase.execute();
        view.printCourses(courses);
    }

    private void handleRelatorios() {
        boolean back = false;
        while (!back) {
            view.printReportsMenu();
            int option = scanner.nextInt();
            scanner.nextLine();
            
            switch (option) {
                case 1 -> {
                    view.printDifficultyLevels();
                    int levelOption = scanner.nextInt();
                    scanner.nextLine();
                    DifficultyLevel level = DifficultyLevel.values()[levelOption - 1];
                    List<Course> courses = relatorioCursosPorNivelUseCase.execute(level);
                    view.printCourses(courses);
                }
                case 2 -> {
                    Set<String> instructors = relatorioInstrutoresAtivosUseCase.execute();
                    view.printInstructors(instructors);
                }
                case 3 -> {
                    Map<SubscriptionPlan, List<Student>> studentsByPlan = relatorioAlunosPorPlanoUseCase.execute();
                    view.printStudentsByPlan(studentsByPlan);
                }
                case 4 -> {
                    double average = relatorioMediaProgressoUseCase.execute();
                    view.printAverageProgress(average);
                }
                case 5 -> {
                    Optional<Student> student = relatorioAlunoMaisMatriculasUseCase.execute();
                    view.printStudentWithMostEnrollments(student);
                }
                case 0 -> back = true;
                default -> view.printError("Opção inválida.");
            }
        }
    }

    private void handleExportarCsv() {
        view.printMessage("Escolha o tipo de dados:");
        view.printMessage("1. Cursos");
        view.printMessage("2. Alunos");
        int type = scanner.nextInt();
        scanner.nextLine();
        
        view.printMessage("Digite as colunas separadas por vírgula (ex: title,description,instructorName):");
        String columnsInput = scanner.nextLine();
        List<String> columns = Arrays.asList(columnsInput.split(","));
        columns = columns.stream().map(String::trim).toList();
        
        try {
            String csv = "";
            if (type == 1) {
                List<Course> courses = consultarCatalogoUseCase.execute();
                csv = GenericCsvExporter.exportToCsv(courses, columns);
            } else if (type == 2) {
                Map<SubscriptionPlan, List<Student>> studentsByPlan = relatorioAlunosPorPlanoUseCase.execute();
                List<Student> allStudents = studentsByPlan.values().stream()
                    .flatMap(List::stream)
                    .toList();
                csv = GenericCsvExporter.exportToCsv(allStudents, columns);
            }
            view.printCsv(csv);
        } catch (Exception e) {
            view.printError("Erro ao exportar CSV: " + e.getMessage());
        }
    }
}

