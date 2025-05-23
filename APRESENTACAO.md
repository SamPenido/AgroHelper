# 🌱 AgroHelper - Marketplace Agrícola
## Projeto Acadêmico Simplificado

### 📋 Resumo do Projeto
**AgroHelper** é um marketplace especializado em produtos agrícolas que conecta produtores rurais, facilitando a compra e venda de:
- 🌾 Grãos e cereais
- 🍎 Frutas e hortaliças  
- 🚜 Equipamentos agrícolas
- 🛠️ Serviços especializados
- 🧪 Insumos agrícolas

---

## 🎯 Funcionalidades Implementadas

### ✅ 1. Backend Java + Spring Boot (30%)
- **Framework**: Spring Boot 3.2.1 com Java 17
- **Banco de Dados**: H2 (em memória) para desenvolvimento
- **APIs REST**: CRUD completo para usuários e produtos
- **Validações**: Bean Validation com anotações
- **CORS**: Configurado para integração frontend

**Endpoints Principais:**
- `POST /api/v1/users/register` - Cadastro de usuário
- `POST /api/v1/users/login` - Login de usuário  
- `GET /api/v1/products` - Listar produtos
- `POST /api/v1/products` - Cadastrar produto
- `GET /api/v1/products/search?keyword=` - Buscar produtos

### ✅ 2. Frontend Integrado ao Backend (30%)
- **Tecnologias**: HTML5, CSS3, JavaScript (Vanilla)
- **Design**: Interface moderna e responsiva
- **Funcionalidades**:
  - Sistema de login/registro conectado ao backend
  - Listagem de produtos via API
  - Cadastro de novos produtos
  - Sistema de busca e filtros
  - Gerenciamento de sessão (localStorage)

### ✅ 3. Banco de Dados Integrado (10%)
- **Banco**: H2 Database (em memória)
- **ORM**: JPA/Hibernate
- **Estrutura**:
  - Tabela `users` (id, email, password, full_name, created_at)
  - Tabela `products` (id, title, description, price, category, location, seller_name, image_url, created_at)

---

## 🚀 Como Executar

### 1. Backend
```bash
cd /home/samuel-penido/dev/sam/agrohelper
mvn spring-boot:run
```
**Acesso**: http://localhost:8080/api/v1

### 2. Frontend
Abrir `frontend/index.html` no navegador ou usar servidor local:
```bash
cd frontend
python -m http.server 8000
```
**Acesso**: http://localhost:8000

### 3. Console H2 (Visualizar Banco)
**URL**: http://localhost:8080/api/v1/h2-console
**JDBC URL**: `jdbc:h2:mem:agrohelper`
**Username**: `sa`
**Password**: *(vazio)*

---

## 🎬 Demonstração para Apresentação

### 1. Cadastro de Usuário
1. Acessar página de registro
2. Preencher dados (nome, email, senha)
3. Mostrar validação e integração com backend

### 2. Login
1. Fazer login com usuário criado
2. Demonstrar redirecionamento automático

### 3. Marketplace
1. Visualizar produtos existentes
2. Demonstrar filtros por categoria
3. Mostrar busca por palavras-chave

### 4. Cadastro de Produto
1. Acessar "Vender"
2. Cadastrar novo produto
3. Verificar aparecimento na listagem

### 5. Backend/Banco
1. Mostrar console H2 com dados persistidos
2. Demonstrar APIs REST via Postman/curl
3. Mostrar logs do Spring Boot

---

## 📊 Critérios de Avaliação Atendidos

| Item | Peso | Status | Observações |
|------|------|--------|-------------|
| **Banco de Dados** | 10% | ✅ COMPLETO | H2 + JPA/Hibernate funcionando |
| **Frontend Integrado** | 30% | ✅ COMPLETO | Login, CRUD produtos funcionando |
| **Backend Integrado** | 30% | ✅ COMPLETO | Spring Boot + APIs REST + Banco |

**Total Implementado**: 70% dos critérios principais

---

## 🔧 Tecnologias Utilizadas

### Backend
- Java 17
- Spring Boot 3.2.1
- Spring Data JPA
- H2 Database
- Bean Validation
- Maven

### Frontend  
- HTML5, CSS3, JavaScript
- Fetch API para comunicação
- LocalStorage para sessões
- Design responsivo

### Banco de Dados
- H2 (desenvolvimento)
- JPA/Hibernate (ORM)
- SQL DDL automático

---

## 🎁 Diferenciais do Projeto

1. **Foco Especializado**: Marketplace específico para agronegócio
2. **Interface Moderna**: Design profissional e intuitivo
3. **Integração Completa**: Frontend ↔ Backend ↔ Banco funcionando
4. **Código Limpo**: Estrutura bem organizada e documentada
5. **Pronto para Expansão**: Base sólida para novas funcionalidades

---

## 📝 Próximos Passos (Futuro)
- Migração para PostgreSQL em produção
- Sistema de autenticação JWT
- Upload de imagens
- Sistema de pagamentos
- Chat entre usuários
- Integração com APIs externas (clima, preços)

---

**Desenvolvido para fins acadêmicos | 2025**