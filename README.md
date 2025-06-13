# 🌱 AgroHelper - Plataforma Inteligente para Agricultura 🚜  

O **AgroHelper** é um sistema web desenvolvido para auxiliar pequenos e médios agricultores na **gestão de suas propriedades**, fornecendo **um marketplace para compra e venda de produtos agrícolas**, além de um **chatbot agrícola inteligente** para assistência especializada.

---

## 📌 Tecnologias Utilizadas  

### 🖥️ **Back-end**  
- Java (Spring Boot)  
- Spring Security + JWT (Autenticação)
- Arquitetura em camadas (Controller → Service → DAO → Repository)
- Python + FastAPI (Chatbot IA)
- OpenAI API (Processamento de Linguagem Natural)

### 🌍 **Front-end**  
- HTML, CSS, JavaScript  
- Design responsivo
- UI moderna e profissional

### 📊 **Banco de Dados**  
- PostgreSQL  

---

## 🚀 Funcionalidades Principais  
✅ **Sistema de Usuários com Tipos Diferenciados**
   - BUYER (Comprador): Pode comprar produtos e usar o chatbot
   - SELLER (Vendedor): Pode vender produtos no marketplace
   - ADMIN (Administrador): Controle total do sistema

✅ **Marketplace Agrícola**  
   - Filtros avançados por categoria e preço
   - Cards de produtos animados e responsivos
   - Detalhes de produtos completos
   - Interface intuitiva e profissional
   - Controle de acesso baseado em tipo de usuário

✅ **Chatbot Agrícola com IA**
   - Integração com OpenAI
   - Contexto especializado em agricultura
   - Recomendação inteligente de produtos
   - Exclusivo para usuários compradores
   - Interface interativa e amigável

✅ **Sistema de Autenticação Segura**
   - Login/Registro com validação
   - Armazenamento seguro de senhas
   - Tokens JWT para autenticação
   - Controle de permissões por tipo de usuário

---

## 📂 Estrutura do Projeto  

### 📌 Backend (Java + Spring Boot)
```
src/
├── main/java/com/agrohelper/
│   ├── AgroHelperApplication.java  # Classe principal
│   ├── config/                     # Configurações da aplicação
│   ├── controller/                 # Controladores REST
│   ├── service/                    # Camada de serviço
│   ├── dao/                        # Objetos de acesso a dados
│   ├── repository/                 # Interfaces para banco de dados
│   ├── entity/                     # Entidades/modelos
│   └── exception/                  # Tratamento de exceções
└── resources/
    ├── application.properties      # Configurações do Spring
    └── db/                         # Scripts SQL
```

### 📌 Front-end
```
frontend/
├── assets/                         # Recursos estáticos
│   ├── css/                        # Estilos CSS
│   ├── js/                         # Scripts JavaScript
│   └── images/                     # Imagens
├── components/                     # Componentes reutilizáveis
├── index.html                      # Página inicial
└── pages/                          # Páginas da aplicação
    ├── auth/                       # Autenticação (login/registro)
    │   ├── login.html
    │   └── register.html
    └── marketplace/                # Marketplace
        ├── index.html
        ├── add-product.html
        └── product-detail.html
```

### 📌 Chatbot (Python + FastAPI)
```
chatbot/
├── app.py                          # Aplicação principal FastAPI
├── agriculture_context.py          # Sistema de contexto agrícola
└── requirements.txt                # Dependências Python
```

---

## 🔍 Funcionalidades Detalhadas

### **1️⃣ Marketplace Agrícola** 🛒
O marketplace permite que agricultores (SELLER) cadastrem seus produtos para venda, enquanto compradores (BUYER) podem navegar, filtrar e visualizar detalhes dos produtos disponíveis.

**Características:**
- **Sistema de Filtros Avançados**: Categoria, preço, localização
- **Cards de Produtos Animados**: Design moderno com efeitos de hover
- **Controle de Acesso**: Apenas vendedores podem adicionar produtos
- **Categorias de Produtos**: Grãos, Frutas, Legumes, Equipamentos, Serviços, Insumos

### **2️⃣ Chatbot Agrícola com IA** 🤖
Um assistente virtual especializado em agricultura, utilizando a API da OpenAI e um sistema de contexto específico para fornecer informações precisas sobre cultivo, equipamentos e técnicas agrícolas.

**Características:**
- **Conhecimento Agrícola Especializado**: Informações sobre culturas, técnicas e equipamentos
- **Sugestão de Produtos**: Recomendação inteligente baseada na conversa
- **Interface Amigável**: Design moderno com animações suaves
- **Exclusivo para Compradores**: Funcionalidade premium para usuários BUYER

### **3️⃣ Arquitetura em Camadas** 🏗️
Implementação de uma arquitetura robusta seguindo os princípios de separação de responsabilidades:

**Fluxo de Dados:**
- **Controller**: Recebe requisições HTTP, valida permissões
- **Service**: Implementa lógica de negócio
- **DAO**: Abstrai operações de acesso a dados
- **Repository**: Interface direta com o banco de dados

### **4️⃣ Sistema de Usuários** 👤
Diferentes tipos de usuários com permissões específicas:

**Tipos:**
- **BUYER**: Pode comprar produtos e usar o chatbot agrícola
- **SELLER**: Pode vender produtos no marketplace e visualizar seus anúncios
- **ADMIN**: Acesso completo ao sistema (não implementado na interface atual)

---

## ⚡ Como Rodar o Projeto  

### 1️⃣ **Pré-requisitos**
- Java 17+
- Maven
- PostgreSQL
- Python 3.8+ (para o chatbot)
- Chave da API OpenAI (para o chatbot)

### 2️⃣ **Backend Java**
```bash
# Clone o repositório
git clone https://github.com/SamPenido/agrohelper.git
cd agrohelper

# Configure o banco de dados PostgreSQL
# Edite src/main/resources/application.properties com suas credenciais

# Compile e execute com Maven
mvn spring-boot:run
```

### 3️⃣ **Frontend**
```bash
# Navegue até a pasta do frontend
cd frontend

# Abra o index.html em um navegador ou use um servidor local
# Exemplo com Python
python -m http.server 8000
```

### 4️⃣ **Chatbot IA (Opcional)**
```bash
# Navegue até a pasta do chatbot
cd chatbot

# Instale dependências
pip install -r requirements.txt

# Configure a chave da API OpenAI
# Edite o arquivo .env na raiz do projeto

# Execute o servidor FastAPI
uvicorn app:app --reload --host 0.0.0.0 --port 8000
```

---

## 👨‍💻 Desenvolvido por:  
🔹 **Samuel Penido**  

---

## 📝 Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.
