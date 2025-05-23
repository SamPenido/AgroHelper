# Status do Projeto AgroHelper (02/04/2025)

## Backend (Java/Spring Boot)

**Componentes Criados/Atualizados:**

*   **Models:** `User.java`, `Product.java` (Estrutura de dados definida com JPA).
*   **Repositories:** `UserRepository.java`, `ProductRepository.java` (Interfaces Spring Data JPA para acesso ao DB).
*   **DTOs:** `RegisterRequestDTO.java`, `LoginRequestDTO.java`, `ProductRequestDTO.java` (Objetos para transferência de dados da API).
*   **Services:** `UserService.java`, `ProductService.java` (Lógica de negócio para usuários e produtos).
*   **Config:**
    *   `application.properties`: Configurado para PostgreSQL em `localhost:5432`, DB `agrohelper_db`, user `postgres`. **Senha precisa ser confirmada/corrigida.** `ddl-auto=update` habilitado.
    *   `SecurityConfig.java`: Configuração básica de segurança com CORS, BCrypt, e regras de acesso (registro/login/produtos públicos, resto autenticado).
*   **Application:** `AgrohelperApplication.java` (Classe principal).
*   **Build:** `pom.xml` atualizado com dependências necessárias (JPA API, Validation, Security, Web, PG Driver) e compatibilidade Java 8.

**Pendências Backend:**

*   **Conexão DB:** Resolver problema de autenticação do usuário `postgres` no PostgreSQL (senha incorreta). Criar o banco de dados `agrohelper_db` manualmente no DBeaver/psql. Atualizar senha no `application.properties`.
*   **Controllers:** Implementar `AuthController.java` e `ProductController.java` para expor os serviços como API REST.
*   **UserDetails:** Implementar `UserDetailsService` para carregar detalhes do usuário para o Spring Security (melhora performance e permite buscar ID no `/api/auth/status`).
*   **Testes:** Implementar testes unitários e de integração.

## Frontend

*   Foram realizadas diversas modificações em arquivos HTML, CSS e JS (não detalhadas).
*   **Problema Identificado:** O formulário de registro (`register.html`) parece estar enviando dados via GET para a própria página HTML (ex: `...register.html?name=...&email=...`), em vez de enviar uma requisição POST para a API backend (`/api/auth/register`) com os dados no corpo da requisição (JSON). Isso precisa ser corrigido no JavaScript do frontend.

## Próximos Passos Imediatos

1.  Resolver conexão com PostgreSQL (senha, criação do DB `agrohelper_db`).
2.  Atualizar senha no `application.properties`.
3.  Implementar `AuthController` e `ProductController`.
4.  Corrigir envio do formulário de registro no frontend (POST para `/api/auth/register`).
