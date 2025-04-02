# Documentação do Backend AgroHelper

Este documento fornece uma visão geral da estrutura e funcionalidade do backend Java para a aplicação AgroHelper, com base no código-fonte no diretório `src/main/java/com/agrohelper`.

## Arquitetura Geral

O backend é uma aplicação Java construída usando o framework **Spring Boot**. Ele segue um padrão comum de arquitetura em camadas:

1.  **Controllers:** Lidam com as requisições HTTP recebidas do frontend ou outros clientes.
2.  **Services:** Contêm a lógica de negócios principal da aplicação.
3.  **Repositories:** Gerenciam a persistência de dados, interagindo com o banco de dados.
4.  **Models (Entidades & Enums):** Definem a estrutura dos dados sendo gerenciados.
5.  **DTOs (Data Transfer Objects):** Usados para transferir dados entre camadas, especialmente entre o frontend e os controllers, frequentemente para validação ou formatação específica de dados.
6.  **Configuration:** Configura componentes do framework, como segurança.

## Estrutura de Pacotes e Propósito

*   **`src/main/java/com/agrohelper/`**
    *   **`AgrohelperApplication.java`**: O ponto de entrada principal da aplicação Spring Boot. Inicializa e executa o contexto da aplicação usando `@SpringBootApplication`.

*   **`src/main/java/com/agrohelper/config/`**
    *   **Propósito:** Contém classes de configuração para a aplicação.
    *   **`SecurityConfig.java`**: Configura o Spring Security (`@Configuration`, `@EnableWebSecurity`).
        *   Define um bean `PasswordEncoder` usando `BCryptPasswordEncoder` para hashing seguro de senhas.
        *   Configura regras de segurança HTTP (`configure(HttpSecurity http)`):
            *   Desabilita a proteção CSRF (`.csrf().disable()`).
            *   Permite acesso não autenticado (`.permitAll()`) a:
                *   `/api/auth/register`
                *   Recursos estáticos do frontend (`/`, `/index.html`, `/assets/**`, `/pages/auth/**`, `/components/**`)
            *   Requer autenticação (`.anyRequest().authenticated()`) para todas as outras requisições.
            *   Configura login baseado em formulário (`.formLogin()`):
                *   Especifica a página de login customizada (`/pages/auth/login.html`).
                *   Define a URL de processamento de login (`/api/auth/login`) tratada pelo Spring Security.
                *   Define a URL de redirecionamento padrão em caso de sucesso (`/pages/dashboard/index.html`).
                *   Permite acesso à página/URL de processamento de login.
            *   Configura logout (`.logout()`):
                *   Define a URL de logout (`/api/auth/logout`).
                *   Define a URL de redirecionamento após o logout (`/pages/auth/login.html?logout`).
                *   Permite acesso à URL de logout.

*   **`src/main/java/com/agrohelper/controllers/`**
    *   **Propósito:** Lida com requisições web recebidas e define endpoints da API (`@RestController`).
    *   **`AuthController.java`**: Define endpoints REST sob o caminho base `/api/auth` (`@RequestMapping("/api/auth")`).
        *   Injeta `UserService` via construtor (`@Autowired`).
        *   **`POST /api/auth/register`**:
            *   Aceita dados de registro de usuário via `@RequestBody RegisterRequestDTO`.
            *   Valida a entrada usando `@Valid`.
            *   Chama `userService.registerUser()` para executar a lógica de registro.
            *   Retorna `ResponseEntity` com status `201 Created` em caso de sucesso ou status de erro apropriado (`400 Bad Request`, `500 Internal Server Error`) em caso de falha.
        *   *(Nota: O endpoint de login está comentado/planejado).*

*   **`src/main/java/com/agrohelper/dto/`**
    *   **Propósito:** Contém Data Transfer Objects (DTOs) usados para estruturar dados para operações específicas, frequentemente para requisições/respostas de API e validação.
    *   **`RegisterRequestDTO.java`**: Define a estrutura para dados recebidos durante o registro de usuário.
        *   Campos: `name`, `email`, `password`, `userType`.
        *   Inclui anotações de validação de `javax.validation.constraints` (`@NotBlank`, `@Email`, `@Size`, `@NotNull`) para garantir a integridade dos dados antes do processamento.

*   **`src/main/java/com/agrohelper/models/`**
    *   **Propósito:** Contém entidades JPA (Java Persistence API) (`@Entity`) que mapeiam para tabelas de banco de dados e enums relacionados. Usa anotações `javax.persistence`.
    *   **`User.java`**: Representa uma entidade de usuário.
        *   `@Id`, `@GeneratedValue(strategy = GenerationType.IDENTITY)`: Define a chave primária (`id`).
        *   `@Column`: Mapeia campos (`name`, `email`, `passwordHash`, `userType`, `createdAt`) para colunas do banco de dados com restrições (ex: `nullable = false`, `unique = true`, `length`).
        *   `email`: Marcado como único.
        *   `userType`: Armazenado como string (`@Enumerated(EnumType.STRING)`).
        *   `createdAt`: Definido automaticamente para a hora atual (`LocalDateTime.now()`).
    *   **`UserType.java`**: Um `enum` definindo os papéis do usuário: `FARMER` (Agricultor), `BUYER` (Comprador), `ADMIN` (Administrador).
    *   **`Farm.java`**: Representa uma entidade de fazenda.
        *   `@Id`, `@GeneratedValue`: Chave primária (`id`).
        *   `@ManyToOne`, `@JoinColumn(name = "user_id")`: Define um relacionamento muitos-para-um com `User`. Cada fazenda pertence a um usuário, vinculada pela coluna de chave estrangeira `user_id`.
        *   Outros campos: `name`, `location`, `areaHectares`, `createdAt`.
    *   **`Crop.java`**: Representa uma entidade de cultura (plantação).
        *   `@Id`, `@GeneratedValue`: Chave primária (`id`).
        *   `@ManyToOne`, `@JoinColumn(name = "farm_id")`: Define um relacionamento muitos-para-um com `Farm`. Cada cultura pertence a uma fazenda, vinculada pela coluna de chave estrangeira `farm_id`.
        *   Outros campos: `cropType`, `plantedArea`, `plantingDate`, `harvestDate`, `status`.
        *   `status`: Um enum (`CropStatus`) armazenado como string, com padrão `ACTIVE`.
    *   **`CropStatus` (enum dentro de `Crop.java`)**: Define os status da cultura: `ACTIVE` (Ativa), `HARVESTED` (Colhida), `CANCELED` (Cancelada).

*   **`src/main/java/com/agrohelper/repositories/`**
    *   **Propósito:** Contém interfaces de repositório Spring Data JPA para interação com o banco de dados.
    *   **`UserRepository.java`**: Interface que estende `JpaRepository<User, Long>`.
        *   Fornece operações CRUD padrão para a entidade `User` automaticamente.
        *   Define métodos de consulta personalizados (`findByEmail`, `existsByEmail`) que o Spring Data implementa com base nas convenções de nomenclatura de métodos.

*   **`src/main/java/com/agrohelper/services/`**
    *   **Propósito:** Contém classes (`@Service`) que implementam a lógica de negócios da aplicação. Usa `@Transactional` para gerenciamento de transações de banco de dados.
    *   **`UserService.java`**: Lida com operações relacionadas ao usuário.
        *   Injeta `UserRepository` e `PasswordEncoder` (`@Autowired`).
        *   **`registerUser(String name, String email, String rawPassword, UserType userType)`**:
            *   Verifica se o email já existe usando `userRepository.existsByEmail()`. Lança `IllegalArgumentException` se existir.
            *   Cria um novo objeto `User`.
            *   Faz o hash da `rawPassword` usando `passwordEncoder.encode()`.
            *   Salva o novo usuário usando `userRepository.save()`.
            *   Retorna o objeto `User` salvo.
        *   **`findByEmail(String email)`**: Recupera um usuário pelo email usando `userRepository.findByEmail()`.

## Resumo da Funcionalidade

A funcionalidade principal implementada nestes arquivos Java estabelece a fundação para uma aplicação web focada em agricultura. As características chave incluem:

*   **Gerenciamento de Usuários:** Registro de diferentes tipos de usuários (Agricultor, Comprador, Admin) com hashing seguro de senha e prevenção de email duplicado.
*   **Modelagem de Dados:** Define a estrutura do banco de dados para Usuários, Fazendas e Culturas, incluindo relacionamentos entre eles (Usuário -> Fazenda -> Cultura).
*   **Endpoints de API:** Expõe uma API REST para registro de usuário (`/api/auth/register`).
*   **Segurança:** Implementa segurança web básica usando Spring Security, protegendo a maioria dos endpoints enquanto permite acesso público ao registro, login e ativos do frontend. Usa autenticação baseada em formulário.

Este backend fornece os componentes necessários para que os usuários se registrem e servirá como base para adicionar mais funcionalidades como login, gerenciamento de fazendas, acompanhamento de culturas e potencialmente um marketplace.
