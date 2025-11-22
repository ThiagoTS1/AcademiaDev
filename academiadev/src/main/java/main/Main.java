package main;

import application.repositories.*;
import application.usecases.*;
import infrastructure.persistence.*;
import infrastructure.ui.ConsoleController;
import infrastructure.ui.ConsoleView;

public class Main {
    public static void main(String[] args) {
        CourseRepository courseRepository = new CourseRepositoryEmMemoria();
        UserRepository userRepository = new UserRepositoryEmMemoria();
        EnrollmentRepository enrollmentRepository = new EnrollmentRepositoryEmMemoria();
        SupportTicketQueue ticketQueue = new SupportTicketQueueEmMemoria();
        
        InitialData.populate(courseRepository, userRepository, enrollmentRepository);
        AtivarCursoUseCase ativarCursoUseCase = new AtivarCursoUseCase(courseRepository);
        DesativarCursoUseCase desativarCursoUseCase = new DesativarCursoUseCase(courseRepository);
        AlterarPlanoAlunoUseCase alterarPlanoAlunoUseCase = new AlterarPlanoAlunoUseCase(userRepository);
        MatricularAlunoUseCase matricularAlunoUseCase = new MatricularAlunoUseCase(
            enrollmentRepository, courseRepository, userRepository);
        ConsultarMatriculasUseCase consultarMatriculasUseCase = new ConsultarMatriculasUseCase(
            enrollmentRepository, userRepository);
        AtualizarProgressoUseCase atualizarProgressoUseCase = new AtualizarProgressoUseCase(
            enrollmentRepository, userRepository);
        CancelarMatriculaUseCase cancelarMatriculaUseCase = new CancelarMatriculaUseCase(
            enrollmentRepository, userRepository);
        AbrirTicketUseCase abrirTicketUseCase = new AbrirTicketUseCase(ticketQueue, userRepository);
        AtenderTicketUseCase atenderTicketUseCase = new AtenderTicketUseCase(ticketQueue);
        ConsultarCatalogoUseCase consultarCatalogoUseCase = new ConsultarCatalogoUseCase(courseRepository);
        GerarRelatorioCursosPorNivelUseCase relatorioCursosPorNivelUseCase = 
            new GerarRelatorioCursosPorNivelUseCase(courseRepository);
        GerarRelatorioInstrutoresAtivosUseCase relatorioInstrutoresAtivosUseCase = 
            new GerarRelatorioInstrutoresAtivosUseCase(courseRepository);
        GerarRelatorioAlunosPorPlanoUseCase relatorioAlunosPorPlanoUseCase = 
            new GerarRelatorioAlunosPorPlanoUseCase(userRepository);
        GerarRelatorioMediaProgressoUseCase relatorioMediaProgressoUseCase = 
            new GerarRelatorioMediaProgressoUseCase(enrollmentRepository);
        GerarRelatorioAlunoMaisMatriculasUseCase relatorioAlunoMaisMatriculasUseCase = 
            new GerarRelatorioAlunoMaisMatriculasUseCase(enrollmentRepository, userRepository);
        
        ConsoleView view = new ConsoleView();
        ConsoleController controller = new ConsoleController(
            view,
            userRepository,
            ativarCursoUseCase,
            desativarCursoUseCase,
            alterarPlanoAlunoUseCase,
            matricularAlunoUseCase,
            consultarMatriculasUseCase,
            atualizarProgressoUseCase,
            cancelarMatriculaUseCase,
            abrirTicketUseCase,
            atenderTicketUseCase,
            consultarCatalogoUseCase,
            relatorioCursosPorNivelUseCase,
            relatorioInstrutoresAtivosUseCase,
            relatorioAlunosPorPlanoUseCase,
            relatorioMediaProgressoUseCase,
            relatorioAlunoMaisMatriculasUseCase
        );
        
        controller.run();
    }
}

