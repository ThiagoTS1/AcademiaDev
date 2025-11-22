# Justificativa de Design - AcademiaDev

Este documento explica as decisões de arquitetura e design do sistema AcademiaDev, focando em como os princípios da Clean Architecture foram aplicados, especialmente a **Regra da Dependência** e o isolamento de detalhes de implementação.

## 1. Regra da Dependência (Dependency Rule)

A **Regra da Dependência** estabelece que as dependências devem apontar sempre para dentro, em direção às camadas mais centrais. No nosso sistema, isso significa:

```
Infrastructure → Application → Domain
```

### 1.1 Estrutura de Camadas

#### **Domain (Camada Central - Mais Pura)**
- **Não depende de NINGUÉM**
- Contém apenas entidades, enums e exceções de negócio
- Zero dependências externas
- Exemplo: `Course`, `Student`, `Enrollment` não conhecem repositórios, UI ou qualquer detalhe de implementação

```java
// Domain não conhece Application ou Infrastructure
public class Course {
    private String title;
    private CourseStatus status;
    
    public void activate() {
        this.status = CourseStatus.ACTIVE;
    }
}
```

#### **Application (Camada Intermediária)**
- **Depende APENAS de Domain**
- Contém interfaces de repositórios (abstrações)
- Contém Use Cases que orquestram a lógica de negócio
- **NUNCA** conhece implementações concretas

```java
// Application conhece apenas Domain e suas próprias abstrações
public class MatricularAlunoUseCase {
    private final EnrollmentRepository enrollmentRepository; // Interface!
    private final CourseRepository courseRepository; // Interface!
    
    // Lógica de negócio usando apenas abstrações
}
```

#### **Infrastructure (Camada Externa)**
- **Depende de Application e Domain**
- Implementa as interfaces definidas em Application
- Contém detalhes de implementação (persistência, UI, utilitários)

```java
// Infrastructure implementa interfaces de Application
public class CourseRepositoryEmMemoria implements CourseRepository {
    private final Map<String, Course> courses = new ConcurrentHashMap<>();
    // Implementação concreta
}
```

### 1.2 Fluxo de Dependências

```
┌─────────────────────────────────────┐
│      Infrastructure Layer           │
│  (ConsoleController, Repositories)  │
└──────────────┬──────────────────────┘
               │ depende de
               ▼
┌─────────────────────────────────────┐
│      Application Layer              │
│  (UseCases, Repository Interfaces)  │
└──────────────┬──────────────────────┘
               │ depende de
               ▼
┌─────────────────────────────────────┐
│        Domain Layer                 │
│  (Entities, Enums, Exceptions)      │
└─────────────────────────────────────┘
```

## 2. Injeção de Dependência no Main.java

O `Main.java` atua como o **Composition Root** do sistema, sendo o único lugar onde todas as camadas são conhecidas e onde a injeção de dependência manual é realizada.

### 2.1 Processo de Inicialização

```java
public class Main {
    public static void main(String[] args) {
        // 1. Criar implementações concretas (Infrastructure)
        CourseRepository courseRepository = new CourseRepositoryEmMemoria();
        UserRepository userRepository = new UserRepositoryEmMemoria();
        // ...
        
        // 2. Popular dados iniciais
        InitialData.populate(courseRepository, userRepository, enrollmentRepository);
        
        // 3. Criar Use Cases injetando interfaces (Application)
        MatricularAlunoUseCase matricularAlunoUseCase = 
            new MatricularAlunoUseCase(enrollmentRepository, courseRepository, userRepository);
        // ...
        
        // 4. Criar UI injetando Use Cases (Infrastructure)
        ConsoleController controller = new ConsoleController(
            view, userRepository, ativarCursoUseCase, ...
        );
        
        // 5. Iniciar aplicação
        controller.run();
    }
}
```

### 2.2 Por que Manual?

- **Simplicidade**: Para um protótipo, DI manual é suficiente
- **Clareza**: Fica explícito onde e como as dependências são criadas
- **Sem Frameworks**: Não adiciona dependências externas desnecessárias
- **Controle Total**: O desenvolvedor tem controle completo sobre o ciclo de vida dos objetos

### 2.3 Benefícios

1. **Inversão de Dependência**: Use Cases dependem de abstrações (interfaces), não de implementações
2. **Testabilidade**: Fácil criar mocks para testes
3. **Flexibilidade**: Trocar implementações (ex: de memória para banco) sem alterar Use Cases

## 3. Camada Domain Mantida Pura

A camada Domain é completamente independente e contém apenas lógica de negócio pura.

### 3.1 Características da Domain

#### **Sem Dependências Externas**
```java
// ✅ CORRETO: Domain puro
public class Student extends User {
    private SubscriptionPlan subscriptionPlan;
    
    public boolean canEnroll(int currentActiveEnrollments) {
        return subscriptionPlan.canEnroll(currentActiveEnrollments);
    }
}

// ❌ ERRADO: Domain não deve fazer isso
// import application.repositories.EnrollmentRepository; // NUNCA!
```

#### **Lógica de Negócio nas Entidades**
```java
public class Enrollment {
    public void updateProgress(int progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        }
        this.progress = progress;
    }
}
```

#### **Exceções de Negócio**
```java
// Domain define exceções específicas do negócio
public class EnrollmentException extends BusinessException {
    public EnrollmentException(String message) {
        super(message);
    }
}
```

### 3.2 Validação da Pureza

- ✅ Domain não importa nada de `application` ou `infrastructure`
- ✅ Domain não conhece persistência, UI ou frameworks
- ✅ Domain contém apenas regras de negócio
- ✅ Domain pode ser testado isoladamente

## 4. Isolamento de Detalhes de Implementação

### 4.1 Persistência em Memória

A persistência é um **detalhe de implementação** isolado na camada Infrastructure.

#### **Abstração (Application)**
```java
// Interface define o CONTRATO
public interface CourseRepository {
    void save(Course course);
    Optional<Course> findByTitle(String title);
    List<Course> findAll();
}
```

#### **Implementação (Infrastructure)**
```java
// Implementação concreta - pode ser trocada sem afetar Use Cases
public class CourseRepositoryEmMemoria implements CourseRepository {
    private final Map<String, Course> courses = new ConcurrentHashMap<>();
    
    @Override
    public void save(Course course) {
        courses.put(course.getTitle(), course);
    }
    // ...
}
```

#### **Uso (Application)**
```java
// Use Case não sabe se é memória, banco, arquivo, etc.
public class ConsultarCatalogoUseCase {
    private final CourseRepository courseRepository; // Interface!
    
    public List<Course> execute() {
        return courseRepository.findActiveCourses(); // Usa abstração
    }
}
```

**Benefício**: Para trocar para banco de dados, basta criar `CourseRepositoryJDBC implements CourseRepository` e alterar apenas o `Main.java`.

### 4.2 Exportação CSV com Reflection

O `GenericCsvExporter` usa Reflection, que é um **detalhe de framework** isolado na Infrastructure.

#### **Isolamento na Infrastructure**
```java
// infrastructure.utils.GenericCsvExporter
public class GenericCsvExporter {
    public static <T> String exportToCsv(List<T> data, List<String> selectedColumns) {
        // Usa Reflection - detalhe de implementação
        Method getter = findGetterMethod(item.getClass(), getterName, columnName);
        // ...
    }
}
```

#### **Application NÃO Conhece CSV**
```java
// Use Cases retornam apenas dados
public class GerarRelatorioCursosPorNivelUseCase {
    public List<Course> execute(DifficultyLevel level) {
        // Retorna List<Course> - não sabe nada sobre CSV
        return courseRepository.findByDifficultyLevel(level)...
    }
}
```

#### **Controller Orquestra**
```java
// ConsoleController (Infrastructure) conhece ambos
public class ConsoleController {
    public void handleExportarCsv() {
        // 1. Chama Use Case para obter dados
        List<Course> courses = consultarCatalogoUseCase.execute();
        
        // 2. Usa GenericCsvExporter para formatar
        String csv = GenericCsvExporter.exportToCsv(courses, columns);
        
        // 3. Exibe resultado
        view.printCsv(csv);
    }
}
```

**Benefício**: 
- Use Cases permanecem puros, sem conhecimento de CSV
- Se precisar mudar para JSON, XML, etc., altera apenas Infrastructure
- Reflection fica isolado longe da lógica de negócio

### 4.3 Interface de Usuário (CLI)

A UI é um **detalhe de apresentação** isolado na Infrastructure.

#### **Separação de Responsabilidades**
```java
// ConsoleView: apenas exibe
public class ConsoleView {
    public void printCourses(List<Course> courses) { ... }
    public void printError(String error) { ... }
}

// ConsoleController: orquestra Use Cases e View
public class ConsoleController {
    private final ConsoleView view;
    private final MatricularAlunoUseCase matricularAlunoUseCase;
    
    public void handleMatricularAluno() {
        try {
            matricularAlunoUseCase.execute(email, courseTitle);
            view.printSuccess("Matrícula realizada!");
        } catch (EnrollmentException e) {
            view.printError(e.getMessage());
        }
    }
}
```

**Benefício**: Para criar uma interface web, basta criar novos controllers/views sem alterar Use Cases.

## 5. Estruturas de Dados Específicas

### 5.1 Map para Unicidade
```java
// Infrastructure escolhe a estrutura adequada
public class CourseRepositoryEmMemoria {
    private final Map<String, Course> courses = new ConcurrentHashMap<>();
    // Map garante título único e busca O(1)
}
```

### 5.2 Set para Unicidade
```java
// Use Case usa Set para garantir instrutores únicos
public Set<String> execute() {
    return courseRepository.findActiveCourses().stream()
        .map(Course::getInstructorName)
        .collect(Collectors.toSet()); // Set elimina duplicatas
}
```

### 5.3 Queue para FIFO
```java
// Infrastructure usa ArrayDeque para garantir FIFO
public class SupportTicketQueueEmMemoria implements SupportTicketQueue {
    private final Queue<SupportTicket> queue = new ArrayDeque<>();
    // ArrayDeque garante ordem FIFO
}
```

## 6. Programação Funcional com Streams

Os relatórios usam Streams API dentro dos Use Cases, mantendo a lógica de negócio na camada Application.

```java
public class GerarRelatorioCursosPorNivelUseCase {
    public List<Course> execute(DifficultyLevel difficultyLevel) {
        return courseRepository.findByDifficultyLevel(difficultyLevel).stream()
            .sorted(Comparator.comparing(Course::getTitle))
            .collect(Collectors.toList());
    }
}
```

**Benefício**: Lógica de transformação fica nos Use Cases, não espalhada pela UI.

## 7. Tratamento de Exceções

### 7.1 Hierarquia de Exceções
```
BusinessException (Domain)
    └── EnrollmentException (Domain)
```

### 7.2 Fluxo de Exceções
```java
// 1. Domain/Application lança exceção de negócio
public class MatricularAlunoUseCase {
    public void execute(...) {
        if (!course.isActive()) {
            throw new EnrollmentException("Curso não está ativo");
        }
    }
}

// 2. Infrastructure captura e exibe mensagem amigável
public class ConsoleController {
    public void handleMatricularAluno() {
        try {
            matricularAlunoUseCase.execute(...);
        } catch (EnrollmentException e) {
            view.printError(e.getMessage()); // Mensagem amigável
        }
    }
}
```

## 8. Resumo das Decisões de Design

| Aspecto | Decisão | Justificativa |
|---------|---------|---------------|
| **Arquitetura** | Clean Architecture | Separação clara de responsabilidades |
| **DI** | Manual no Main | Simplicidade e controle |
| **Persistência** | Interfaces + Implementações | Flexibilidade para trocar implementação |
| **CSV** | Reflection isolado em Infrastructure | Use Cases não conhecem detalhes |
| **UI** | CLI isolada em Infrastructure | Fácil criar outras interfaces |
| **Estruturas** | Map, Set, Queue conforme necessidade | Eficiência e garantias de comportamento |
| **Streams** | Nos Use Cases | Lógica de transformação centralizada |
| **Exceções** | Hierarquia em Domain | Exceções de negócio bem definidas |

## 9. Conclusão

O sistema AcademiaDev segue rigorosamente a Clean Architecture:

1. ✅ **Regra da Dependência**: Dependências apontam sempre para dentro
2. ✅ **Domain Puro**: Sem dependências externas
3. ✅ **Detalhes Isolados**: Persistência, CSV, UI isolados em Infrastructure
4. ✅ **DI Manual**: Composition Root no Main.java
5. ✅ **Abstrações**: Use Cases dependem de interfaces, não implementações

Essas decisões garantem:
- **Manutenibilidade**: Fácil adicionar novas funcionalidades
- **Testabilidade**: Fácil criar testes unitários
- **Flexibilidade**: Fácil trocar implementações
- **Escalabilidade**: Fácil evoluir o sistema

