# AcademiaDev - Plataforma de Cursos Online

Sistema de gerenciamento de cursos online desenvolvido em Java seguindo os princípios da Clean Architecture.

## 📋 Descrição

AcademiaDev é uma plataforma de cursos online baseada em assinatura que oferece cursos de desenvolvimento de software. O sistema permite gerenciar cursos, usuários (administradores e alunos), matrículas, tickets de suporte e geração de relatórios.

## 🏗️ Arquitetura

O projeto segue a **Clean Architecture** com as seguintes camadas:

### Domain (Camada de Domínio)
- **Entidades**: `Course`, `User`, `Admin`, `Student`, `SubscriptionPlan`, `BasicPlan`, `PremiumPlan`, `Enrollment`, `SupportTicket`
- **Enums**: `DifficultyLevel`, `CourseStatus`
- **Exceções**: `BusinessException`, `EnrollmentException`

### Application (Camada de Aplicação)
- **Use Cases**: Contém toda a lógica de negócio dos casos de uso
- **Repositories (Interfaces)**: Define contratos para persistência de dados

### Infrastructure (Camada de Infraestrutura)
- **Persistence**: Implementações em memória dos repositórios
- **UI**: Interface de linha de comando (`ConsoleView`, `ConsoleController`)
- **Utils**: Utilitários como `GenericCsvExporter` (usa Reflection)

### Main (Ponto de Entrada)
- **Main.java**: Composition Root com Dependency Injection manual
- **InitialData.java**: Popula dados iniciais na memória

## 🚀 Como Executar

### Pré-requisitos
- Java 8 ou superior
- Compilador Java (javac)

### Compilação e Execução

```bash
# Compilar o projeto
javac -d out -sourcepath src/main/java src/main/java/main/Main.java

# Executar
java -cp out main.Main
```

Ou usando um IDE como IntelliJ IDEA ou Eclipse:
1. Importe o projeto
2. Execute a classe `main.Main`

## 👥 Usuários Pré-cadastrados

### Administradores
- Email: `admin@academiadev.com`
- Email: `admin2@academiadev.com`

### Alunos
- Email: `aluno1@email.com` (Plano: BasicPlan)
- Email: `aluno2@email.com` (Plano: PremiumPlan)
- Email: `aluno3@email.com` (Plano: BasicPlan)
- Email: `aluno4@email.com` (Plano: PremiumPlan)

## 📚 Funcionalidades

### Operações de Administrador
1. **Gerenciar Status de Cursos**: Ativar/desativar cursos
2. **Gerenciar Planos de Alunos**: Alterar plano de assinatura de alunos
3. **Atender Tickets de Suporte**: Processar tickets em ordem FIFO
4. **Gerar Relatórios e Análises**: Acessar todos os relatórios da plataforma
5. **Exportar Dados**: Gerar CSV com colunas selecionáveis dinamicamente

### Operações do Aluno
1. **Matricular-se em Curso**: Matricular em cursos ativos (respeitando limites do plano)
2. **Consultar Matrículas**: Ver todos os cursos matriculados e progresso
3. **Atualizar Progresso**: Modificar porcentagem de conclusão (0-100%)
4. **Cancelar Matrícula**: Remover matrícula de um curso

### Operações Gerais
1. **Consultar Catálogo de Cursos**: Listar cursos ativos disponíveis
2. **Abrir Ticket de Suporte**: Criar novo ticket na fila
3. **Autenticação**: Login simples por email (sem senha)

## 📊 Relatórios Disponíveis

1. **Cursos por Nível de Dificuldade**: Lista cursos filtrados por nível, ordenados alfabeticamente
2. **Instrutores de Cursos Ativos**: Lista única de instrutores que lecionam cursos ativos
3. **Alunos Agrupados por Plano**: Agrupa alunos conforme seu plano de assinatura
4. **Média de Progresso Geral**: Calcula a média de progresso de todas as matrículas
5. **Aluno com Mais Matrículas Ativas**: Identifica o aluno com maior número de matrículas ativas

## 🔧 Tecnologias e Conceitos Utilizados

- **Java 8+**: Streams API para processamento funcional
- **Clean Architecture**: Separação de responsabilidades em camadas
- **Dependency Injection**: Injeção manual de dependências no Main
- **Collections**: Map, Set, Queue (ArrayDeque) para estruturas de dados
- **Reflection**: Para exportação genérica de CSV
- **Programação Orientada a Objetos**: Herança, polimorfismo, encapsulamento

## 📝 Regras de Negócio

### Matrículas
- Aluno só pode se matricular se:
  - O plano permitir (BasicPlan: máximo 3 ativas, PremiumPlan: ilimitado)
  - O curso estiver ATIVO
  - O aluno não estiver já matriculado no mesmo curso
- Progresso inicial: 0%
- Progresso pode ser atualizado de 0% a 100%

### Tickets de Suporte
- Processamento em ordem FIFO (First-In, First-Out)
- Qualquer usuário pode abrir tickets
- Apenas administradores podem processar tickets

### Cursos
- Título deve ser único na plataforma
- Cursos INACTIVE não podem receber novas matrículas
- Níveis de dificuldade: BEGINNER, INTERMEDIATE, ADVANCED

## 🎯 Estrutura de Diretórios

```
src/main/java/
├── domain/
│   ├── entities/
│   ├── enums/
│   └── exceptions/
├── application/
│   ├── usecases/
│   └── repositories/
├── infrastructure/
│   ├── persistence/
│   ├── ui/
│   └── utils/
└── main/
    ├── Main.java
    └── InitialData.java
```

## 📄 Licença

Este projeto foi desenvolvido como parte de um desafio acadêmico.

